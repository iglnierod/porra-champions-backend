package com.iglnierod.porra_champions.repository;

import com.iglnierod.porra_champions.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    // Consulta para obtener los distintos valores de matchDay (Jornada)
    @Query("SELECT DISTINCT m.matchDay FROM Match m")
    List<Integer> findDistinctMatchDays();

    List<Match> findByMatchDay(int matchDay);
}
