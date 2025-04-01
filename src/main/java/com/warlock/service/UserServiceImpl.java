package com.warlock.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.repository.UserRepository;
import com.warlock.repository.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    private static final String DEFAULT_ROLE = "user";

    //Получаем весь список пользователей
    @Override
    public @NonNull List<User> findAll() {
        return userRepository.findAll();
    }

    //Получаем пользователя по id
    @Override
    public @NonNull User findById(@NonNull Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + userId + " is not found"));
    }

    //Создаем пользователя
    @Override
    @Transactional
    public @NonNull User createUser(@NonNull User request) {
        Role role = roleRepository.findByName(DEFAULT_ROLE)
                    .orElseThrow(() -> new EntityNotFoundException("Role " + DEFAULT_ROLE + " is not found"));
        request.setRole(role);
        return userRepository.save(request);
    }

    //Обновляем пользователя по id
    @Override
    @Transactional
    public @NonNull User update(@NonNull Long userId, @NonNull User request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + userId + " is not found"));
        userUpdate(user, request);
        return userRepository.save(user);
    }

    //Удаляем пользователя по id
    @Override
    public void delete(@NonNull Long userId) {
        userRepository.deleteById(userId);
    }

    private void userUpdate(@NonNull User user, @NonNull User request) {
        ofNullable(request.getNickname()).map(user::setNickname);
        ofNullable(request.getLogin()).map(user::setLogin);
        ofNullable(request.getPassword()).map(user::setPassword);
        ofNullable(request.getEmail()).map(user::setEmail);
        ofNullable(request.getBio()).map(user::setBio);
        ofNullable(request.getUrl_image()).map(user::setUrl_image);
        ofNullable(request.getSmallThumbnailUrl()).map(user::setSmallThumbnailUrl);
        ofNullable(request.getMediumThumbnailUrl()).map(user::setMediumThumbnailUrl);
        ofNullable(request.getLargeThumbnailUrl()).map(user::setLargeThumbnailUrl);
    }

    @Override
    @Transactional
    public @NonNull User assignRole(@NonNull Long userId, @NonNull Role role){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " is not found"));
        user.setRole(role);
        return userRepository.save(user);
    }
}