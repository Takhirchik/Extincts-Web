package com.warlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.warlock.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Query(value = """
        SELECT u.*, ts_rank(u.search_vector, to_tsquery('russian', :query)) as rank
        FROM users u
        WHERE u.search_vector @@ to_tsquery('russian', :query)
        ORDER BY ts_rank(u.search_vector, to_tsquery('russian', :query)) END DESC
        """, nativeQuery = true)
    List<User> searchUsers(@Param("query") String query);

    @Query(value = "SELECT COUNT(*) FROM users WHERE search_vector @@ to_tsquery('russian', :query)",
            nativeQuery = true)
    int countSearchUsers(@Param("query") String query);

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);
}