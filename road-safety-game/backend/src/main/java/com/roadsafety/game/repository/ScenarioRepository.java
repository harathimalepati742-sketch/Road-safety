package com.roadsafety.game.repository;

import com.roadsafety.game.model.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    List<Scenario> findByCategoryIgnoreCaseOrderByIdAsc(String category);
}
