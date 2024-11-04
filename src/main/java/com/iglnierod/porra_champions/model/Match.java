package com.iglnierod.porra_champions.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match")
public class Match {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "id_local")
  private Team localTeam;

  @ManyToOne
  @JoinColumn(name = "id_away")
  private Team awayTeam;

  private int localGoals;
  private int awayGoals;

  @ManyToOne
  @JoinColumn(name = "matchday_id")
  private Matchday matchday;
}
