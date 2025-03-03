package com.warlock.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.model.request.CreateRoleRequest;
import com.warlock.model.request.CreateUserRequest;
import com.warlock.model.response.RoleResponse;
import com.warlock.model.response.UserResponse;
import com.warlock.repository.UserRepository;
import com.warlock.repository.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private static final String DEFAULT_ROLE = "user";

    //Получаем весь список пользователей
    @Override
    @Transactional(readOnly = true)
    public @NonNull List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::buildUserResponse)
                .collect(Collectors.toList());
    }

    //Получаем пользователя по id
    @Override
    @Transactional(readOnly = true)
    public @NonNull UserResponse findById(@NonNull Long userId) {
        return userRepository.findById(userId)
                .map(this::buildUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User " + userId + " is not found"));
    }

    //Создаем пользователя
    @Override
    @Transactional
    public @NonNull UserResponse createUser(@NonNull CreateUserRequest request) {
        Role role;
        if (request.getRole() == null){
            role = roleRepository.findByName(DEFAULT_ROLE)
                    .orElseThrow(() -> new EntityNotFoundException("Role user not found"));
        } else{
            String roleName = request.getRole().getName();
            role = roleRepository.findByName(roleName)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Role " + roleName + " not found"));
        }
        User user = buildUserRequest(request, role);
        return buildUserResponse(userRepository.save(user));
    }

    //Обновляем пользователя по id
    @Override
    @Transactional
    public @NonNull UserResponse update(@NonNull Long userId, @NonNull CreateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + userId + " is not found"));
        userUpdate(user, request);
        if (user.getRole() != null){
            String roleName = user.getRole().getName();
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new EntityNotFoundException(("Role " + roleName + " is not found")));
            user.setRole(role);
        }
        return buildUserResponse(userRepository.save(user));
    }

    //Удаляем пользователя по id
    @Override
    @Transactional
    public void delete(@NonNull Long userId) {
        userRepository.deleteById(userId);
    }

    private UserResponse buildUserResponse(@NonNull User user) {
        return new UserResponse()
                .setId(user.getId())
                .setNickname(user.getNickname())
                .setLogin(user.getLogin())
                .setPassword(user.getPassword())
                .setEmail(user.getEmail())
                .setRole(new RoleResponse()
                        .setId(user.getRole().getId())
                        .setName(user.getRole().getName()));
    }

    private User buildUserRequest(@NonNull CreateUserRequest request, @NonNull Role role) {
        return new User()
                .setNickname(request.getNickname())
                .setLogin(request.getLogin())
                .setPassword(request.getPassword())
                .setEmail(request.getEmail())
                .setRole(role);
    }

    private void userUpdate(@NonNull User user, @NonNull CreateUserRequest request) {
        ofNullable(request.getNickname()).map(user::setNickname);
        ofNullable(request.getLogin()).map(user::setLogin);
        ofNullable(request.getPassword()).map(user::setPassword);
        ofNullable(request.getEmail()).map(user::setEmail);

        CreateRoleRequest roleRequest = request.getRole();
        if (roleRequest != null) {
            ofNullable(roleRequest.getName()).map(user.getRole()::setName);
        }
    }
}