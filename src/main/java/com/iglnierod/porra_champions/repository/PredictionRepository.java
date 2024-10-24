package com.iglnierod.porra_champions.repository;

import com.iglnierod.porra_champions.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    // Buscar predicciones por el ID del partido
    List<Prediction> findByMatch_Id(Long matchId);

    // Encontrar predicciones por el ID del partido y el usuario
    List<Prediction> findByMatch_IdAndUser_Id(Long matchId, Long userId);
}
