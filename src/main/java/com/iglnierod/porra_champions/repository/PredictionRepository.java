package com.iglnierod.porra_champions.repository;

import com.iglnierod.porra_champions.model.Prediction;
import com.iglnierod.porra_champions.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
}
