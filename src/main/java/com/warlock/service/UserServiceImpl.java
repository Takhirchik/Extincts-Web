package com.warlock.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

    private final UserRepository userRepository;
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
        Role role;
        if (request.getRole().getName() == null){
            role = roleRepository.findByName(DEFAULT_ROLE)
                    .orElseThrow(() -> new EntityNotFoundException("Role " + DEFAULT_ROLE + " is not found"));
        } else{
            String roleName = request.getRole().getName();
            role = roleRepository.findByName(roleName)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Role " + roleName + " is not found"));
        }
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
        ofNullable(request.getRole()).map(user::setRole);
    }
}