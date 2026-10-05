package com.roadsafety.game.service;

import com.roadsafety.game.dto.Dtos.*;
import com.roadsafety.game.exception.ApiException;
import com.roadsafety.game.model.AnswerOption;
import com.roadsafety.game.model.Attempt;
import com.roadsafety.game.model.Player;
import com.roadsafety.game.model.Scenario;
import com.roadsafety.game.repository.AttemptRepository;
import com.roadsafety.game.repository.PlayerRepository;
import com.roadsafety.game.repository.ScenarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/** All game rules live here: scoring, validation of answers, progress and leaderboard. */
@Service
public class GameService {

    public static final int POINTS_PER_CORRECT_ANSWER = 10;

    private final PlayerRepository playerRepository;
    private final ScenarioRepository scenarioRepository;
    private final AttemptRepository attemptRepository;

    public GameService(PlayerRepository playerRepository,
                       ScenarioRepository scenarioRepository,
                       AttemptRepository attemptRepository) {
        this.playerRepository = playerRepository;
        this.scenarioRepository = scenarioRepository;
        this.attemptRepository = attemptRepository;
    }

    @Transactional
    public PlayerResponse createPlayer(PlayerRequest request) {
        Player player = new Player();
        player.setName(request.name().trim());
        player = playerRepository.save(player);
        return new PlayerResponse(player.getId(), player.getName(), player.getTotalScore());
    }

    @Transactional(readOnly = true)
    public List<ScenarioDto> getScenarios(String category) {
        boolean all = category == null || category.isBlank() || category.equalsIgnoreCase("all");
        List<Scenario> scenarios = all
                ? scenarioRepository.findAll(Sort.by("id"))
                : scenarioRepository.findByCategoryIgnoreCaseOrderByIdAsc(category);
        return scenarios.stream().map(this::toDto).toList();
    }

    @Transactional
    public AttemptResponse submitAnswer(AttemptRequest request) {
        Player player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "Player " + request.playerId() + " not found"));
        Scenario scenario = scenarioRepository.findById(request.scenarioId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "Scenario " + request.scenarioId() + " not found"));

        AnswerOption chosen = scenario.getOptions().stream()
                .filter(o -> o.getId().equals(request.optionId()))
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                        "Option " + request.optionId() + " does not belong to this scenario"));

        if (attemptRepository.existsByPlayerIdAndScenarioId(player.getId(), scenario.getId())) {
            throw new ApiException(HttpStatus.CONFLICT, "You already answered this scenario");
        }

        AnswerOption correctOption = scenario.getOptions().stream()
                .filter(AnswerOption::isCorrect)
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Scenario has no correct option configured"));

        int points = chosen.isCorrect() ? POINTS_PER_CORRECT_ANSWER : 0;

        Attempt attempt = new Attempt();
        attempt.setPlayerId(player.getId());
        attempt.setScenarioId(scenario.getId());
        attempt.setOptionId(chosen.getId());
        attempt.setCorrect(chosen.isCorrect());
        attempt.setPoints(points);
        attemptRepository.save(attempt);

        player.setTotalScore(player.getTotalScore() + points);
        playerRepository.save(player);

        return new AttemptResponse(chosen.isCorrect(), points, player.getTotalScore(),
                chosen.getFeedback(), scenario.getTip(),
                correctOption.getId(), correctOption.getOptionText());
    }

    @Transactional(readOnly = true)
    public ProgressResponse getProgress(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "Player " + playerId + " not found"));
        long total = scenarioRepository.count();
        long answered = attemptRepository.countByPlayerId(playerId);
        long correct = attemptRepository.countByPlayerIdAndCorrectTrue(playerId);
        int percent = total == 0 ? 0 : (int) (answered * 100 / total);
        return new ProgressResponse(player.getId(), player.getName(), player.getTotalScore(),
                answered, correct, total, percent);
    }

    @Transactional(readOnly = true)
    public List<LeaderboardEntry> getLeaderboard() {
        List<Player> top = playerRepository
                .findTop10ByTotalScoreGreaterThanOrderByTotalScoreDescCreatedAtAsc(0);
        return IntStream.range(0, top.size())
                .mapToObj(i -> new LeaderboardEntry(i + 1, top.get(i).getName(), top.get(i).getTotalScore()))
                .toList();
    }

    /** Converts to a DTO without the correct flag, and shuffles options so the answer isn't always in the same spot. */
    private ScenarioDto toDto(Scenario s) {
        List<OptionDto> options = new ArrayList<>(s.getOptions().stream()
                .map(o -> new OptionDto(o.getId(), o.getOptionText()))
                .toList());
        Collections.shuffle(options);
        return new ScenarioDto(s.getId(), s.getTitle(), s.getDescription(),
                s.getCategory(), s.getEmoji(), options);
    }
}
