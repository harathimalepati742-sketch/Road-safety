package com.roadsafety.game.repository;

import com.roadsafety.game.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    boolean existsByPlayerIdAndScenarioId(Long playerId, Long scenarioId);

    long countByPlayerId(Long playerId);

    long countByPlayerIdAndCorrectTrue(Long playerId);
}
