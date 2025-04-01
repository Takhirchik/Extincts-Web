package com.warlock.repository;

import com.warlock.domain.Stand;
import com.warlock.domain.StandStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StandStatsRepository extends JpaRepository<StandStats, Long> {
    // Найти статистику по Stand и Date
    Optional<StandStats> findByStandAndDate(Stand stand, LocalDate date);
    // Найти всю статистику Stand
    List<StandStats> findByStand(Stand stand);
    // Найти популярные Stands
    @Query("SELECT s.stand.id FROM StandStats s " +
            "WHERE s.date >= :since " +
            "GROUP BY s.stand.id " +
            "ORDER BY SUM(s.views) DESC")
    List<Long> findPopularStands(@Param("since") LocalDate since);

    void deleteByDate(LocalDate date);
}
