package com.roadsafety.game.controller;

import com.roadsafety.game.dto.Dtos.*;
import com.roadsafety.game.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /** GET /api/scenarios?category=Cycling  (category optional) */
    @GetMapping("/scenarios")
    public List<ScenarioDto> scenarios(@RequestParam(required = false) String category) {
        return gameService.getScenarios(category);
    }

    /** POST /api/players  {"name": "Asha"}  -> starts a new game */
    @PostMapping("/players")
    public ResponseEntity<PlayerResponse> createPlayer(@Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createPlayer(request));
    }

    /** POST /api/attempts  {"playerId":1,"scenarioId":2,"optionId":5}  -> feedback + score */
    @PostMapping("/attempts")
    public ResponseEntity<AttemptResponse> submit(@Valid @RequestBody AttemptRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.submitAnswer(request));
    }

    /** GET /api/players/1/progress */
    @GetMapping("/players/{id}/progress")
    public ProgressResponse progress(@PathVariable Long id) {
        return gameService.getProgress(id);
    }

    /** GET /api/leaderboard  -> top 10 */
    @GetMapping("/leaderboard")
    public List<LeaderboardEntry> leaderboard() {
        return gameService.getLeaderboard();
    }
}
