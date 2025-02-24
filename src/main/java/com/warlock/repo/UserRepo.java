package com.warlock.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.warlock.model.User;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    Iterable<User> findAllByNickname(Iterable<String> nicknames);

    Iterable<User> findAllByLogin(Iterable<String> logins);

    Iterable<User> findAllByEmail(Iterable<String> email);

    void deleteByLogin(String login);

    void deleteByEmail(String email);

    void deleteAllByLogin(Iterable<String> logins);

    void deleteAllByEmail(Iterable<String> emails);

    void editUser(User user);

    void editAllUser(Iterable<User> user);

}