package com.roadsafety.game.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

/** All request/response shapes in one place. Note: ScenarioDto never reveals the correct answer. */
public final class Dtos {

    private Dtos() {}

    public record PlayerRequest(
            @NotBlank(message = "Name is required")
            @Size(max = 30, message = "Name must be at most 30 characters")
            String name) {}

    public record PlayerResponse(Long id, String name, int totalScore) {}

    public record OptionDto(Long id, String text) {}

    public record ScenarioDto(Long id, String title, String description,
                              String category, String emoji, List<OptionDto> options) {}

    public record AttemptRequest(
            @NotNull(message = "playerId is required") Long playerId,
            @NotNull(message = "scenarioId is required") Long scenarioId,
            @NotNull(message = "optionId is required") Long optionId) {}

    public record AttemptResponse(boolean correct, int pointsEarned, int totalScore,
                                  String feedback, String tip,
                                  Long correctOptionId, String correctOptionText) {}

    public record ProgressResponse(Long playerId, String playerName, int totalScore,
                                   long answered, long correct, long totalScenarios,
                                   int percentComplete) {}

    public record LeaderboardEntry(int rank, String name, int score) {}

    public record ErrorResponse(int status, String message, LocalDateTime timestamp) {}
}
