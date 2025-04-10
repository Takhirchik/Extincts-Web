package com.warlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.warlock.domain.Stand;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandRepository extends JpaRepository<Stand, Long> {
    Optional<Stand> findByStandName (String standName);

    @Query(value = """
        SELECT s.*, ts_rank(s.search_vector, to_tsquery('russian', :query)) as rank
        FROM stands s
        WHERE s.search_vector @@ to_tsquery('russian', :query)
        ORDER BY 
            CASE WHEN :sortBy = 'relevance' THEN ts_rank(s.search_vector, to_tsquery('russian', :query)) END DESC,
            CASE WHEN :sortBy = 'date' THEN s.created_at END DESC,
            CASE WHEN :sortBy = 'views' THEN s.views END DESC
        """, nativeQuery = true)
    List<Stand> searchStands(
            @Param("query") String query,
            @Param("sortBy") String sortBy);

    @Query(value = "SELECT COUNT(*) FROM stands WHERE search_vector @@ to_tsquery('russian', :query)",
            nativeQuery = true)
    int countSearchStands(@Param("query") String query);


}
