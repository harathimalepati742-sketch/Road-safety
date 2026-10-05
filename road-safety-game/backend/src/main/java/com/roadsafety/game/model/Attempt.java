package com.roadsafety.game.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** A player's answer to one scenario. A player can answer each scenario only once. */
@Entity
@Table(name = "attempts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "scenario_id"}))
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long playerId;

    @Column(nullable = false)
    private Long scenarioId;

    @Column(nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private boolean correct;

    @Column(nullable = false)
    private int points;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    public Long getScenarioId() { return scenarioId; }
    public void setScenarioId(Long scenarioId) { this.scenarioId = scenarioId; }
    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }
    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
