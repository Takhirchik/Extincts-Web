package com.warlock.service;

import com.warlock.repository.RoleRepository;
import jakarta.persistence.EntityExistsException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.warlock.domain.Role;
import com.warlock.domain.User;
import com.warlock.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    private static final String ADMIN_ROLE = "ROLE_ADMIN";

    /**
     * Поиск всех пользователей
     *
     * @return список User
     */
    @Cacheable(key = "'all'")
    @Override
    public @NonNull List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Поиск пользователя по id
     *
     * @param userId id-идентификатор пользователя
     * @return User
     */
    @Cacheable(key = "#userId")
    @Override
    public @NonNull User findById(@NonNull Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + userId + " is not found"));
    }

    /**
     * Сохранение пользователя
     *
     * @param user пользователь
     * @return сохранённый пользователь
     */
    @CachePut(key = "#result.id")
    @Override
    @Transactional
    public @NonNull User save(@NonNull User user){
        return userRepository.save(user);
    }

    /**
     * Создание пользователя
     *
     * @param request пользователь
     * @return созданный пользователь
     */
    @Override
    public @NonNull User create(@NonNull User request){
        if (userRepository.existsByLogin(request.getLogin())){
            throw new EntityExistsException("User with login " + request.getLogin() + " already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())){
            throw new EntityExistsException("User with email " + request.getEmail() + " already exists");
        }
        log.info("Trying to save user: {}", request);
        return save(request);
    }

    /**
     * Обновление пользователя
     *
     * @param userId id-пользователя, которого нужно обновить
     * @param request данные для обновления
     * @return User
     */
    @CacheEvict(key = "#userId")
    @Override
    @Transactional
    public @NonNull User update(@NonNull Long userId, @NonNull User request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + userId + " is not found"));
        log.info("Trying to update user: {} -> {}", user, request);
        userUpdate(user, request);
        return save(user);
    }

    /**
     * Удаление пользователя
     *
     * @param userId id удаляемого пользователя
     */
    @CacheEvict(key = "#userId")
    @Override
    public void delete(@NonNull Long userId) {
        log.info("Deleting user with id: {}", userId);
        userRepository.deleteById(userId);
    }

    /**
     * Вспомогательный метод обновления
     *
     * @param user пользователь из БД
     * @param request данные для обновления
     */
    private void userUpdate(@NonNull User user, @NonNull User request) {
        ofNullable(request.getNickname()).map(user::setNickname);
        ofNullable(request.getLogin()).map(user::setLogin);
        ofNullable(request.getPassword()).map(user::setPassword);
        ofNullable(request.getEmail()).map(user::setEmail);
        ofNullable(request.getBio()).map(user::setBio);
        ofNullable(request.getUrlImage()).map(user::setUrlImage);
        ofNullable(request.getSmallThumbnailUrl()).map(user::setSmallThumbnailUrl);
        ofNullable(request.getMediumThumbnailUrl()).map(user::setMediumThumbnailUrl);
        ofNullable(request.getLargeThumbnailUrl()).map(user::setLargeThumbnailUrl);
    }

    /**
     * Назначение роли пользователю
     *
     * @param userId id-пользователя
     * @return User
     */
//    @CachePut(value = "users", key = "#id")
    @Override
    @Transactional
    public @NonNull User assignRole(@NonNull Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " is not found"));
        Role role = roleRepository.findByName(ADMIN_ROLE)
                        .orElseThrow(() -> new EntityNotFoundException("Role with name " + ADMIN_ROLE + " is not found"));
        log.info("Assign {} to user {}", role, user);
        user.setRole(role);
        return save(user);
    }

    /**
     * Найти пользователя по логину (нужен для UserDetailsService)
     *
     * @param login логин пользователя
     * @return User
     * @throws UsernameNotFoundException ошибка нахождения пользователя
     */
    @Cacheable(key = "'login:' + #login")
    @Override
    @Transactional(readOnly = true)
    public @NonNull User getByLogin(@NonNull String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login));
    }

    /**
     * Конструктор сервиса
     *
     * @return UserDetailsService
     */
    public UserDetailsService userDetailsService(){
        return this::getByLogin;
    }

    /**
     * Получить текущего пользователя
     *
     * @return User
     */
    @Cacheable(key = "'current'")
    @Override
    public @NonNull User getCurrentUser(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        return getByLogin(auth.getName());
    }

    @CachePut(key = "#userId")
    @Override
    public void updateUserImage(
            @NonNull Long userId,
            @NonNull String originalUrl,
            @NonNull String smallThumbnailUrl,
            @NonNull String mediumThumbnailUrl,
            @NonNull String largeThumbnailUrl
    ){
        log.info("Uploading image URL's to user with id {}", userId);
        var user = findById(userId)
                .setUrlImage(originalUrl)
                .setSmallThumbnailUrl(smallThumbnailUrl)
                .setMediumThumbnailUrl(mediumThumbnailUrl)
                .setLargeThumbnailUrl(largeThumbnailUrl);
        save(user);
    }
}