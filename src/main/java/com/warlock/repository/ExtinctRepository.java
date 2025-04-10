package com.warlock.repository;

import com.warlock.domain.Stand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.warlock.domain.Extinct;

import java.util.List;

@Repository
public interface ExtinctRepository extends JpaRepository<Extinct, Long> {
    @Query(value = """
        SELECT e.*, ts_rank(e.search_vector, to_tsquery('russian', :query)) as rank
        FROM extincts e
        WHERE e.search_vector @@ to_tsquery('russian', :query)
        ORDER BY 
            CASE WHEN :sortBy = 'relevance' THEN ts_rank(e.search_vector, to_tsquery('russian', :query)) END DESC,
            CASE WHEN :sortBy = 'date' THEN e.created_at END DESC,
            CASE WHEN :sortBy = 'views' THEN e.views END DESC,
            CASE WHEN :sortBy = 'likes' THEN e.likes END DESC
        """, nativeQuery = true)
    List<Extinct> searchExtincts(
            @Param("query") String query,
            @Param("sortBy") String sortBy);

    @Query(value = "SELECT COUNT(*) FROM extincts WHERE search_vector @@ to_tsquery('russian', :query)",
            nativeQuery = true)
    int countSearchExtincts(@Param("query") String query);

    @Query("SELECT e FROM Extinct e WHERE e.stand = :stand ORDER BY e.createdAt ASC")
    List<Extinct> findAllExtinctsByStandSortedByCreatedAt(@Param("stand") Stand stand);
}
