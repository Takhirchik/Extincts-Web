package com.warlock.repository;

import com.warlock.domain.Extinct;
import com.warlock.domain.ExtinctStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExtinctStatsRepository extends JpaRepository<ExtinctStats, Long> {
    // Найти статистику по Extinct и Date
    Optional<ExtinctStats> findByExtinctAndDate(Extinct extinct, LocalDate date);
    // Найти всю статистику Extinct
    List<ExtinctStats> findByExtinct(Extinct extinct);
    // Найти популярные Extincts
    @Query("SELECT e.extinct.id FROM ExtinctStats e " +
            "WHERE e.date >= :since " +
            "GROUP BY e.extinct.id " +
            "ORDER BY (SUM(e.views) + SUM(e.likes) * 2) DESC")
    List<Long> findPopularExtincts(@Param("since") LocalDate since);

    void deleteByDate(LocalDate date);

}
