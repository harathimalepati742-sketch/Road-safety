package com.roadsafety.game.repository;

import com.roadsafety.game.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    /** Top 10 players who have scored something (ties: earlier player first). */
    List<Player> findTop10ByTotalScoreGreaterThanOrderByTotalScoreDescCreatedAtAsc(int minScore);
}
