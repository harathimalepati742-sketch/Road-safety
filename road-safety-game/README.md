# 🚦 Road Safety Hero: Interactive Road Safety Education Game for Kids

A simple full-stack game where kids pick a topic, face real road-safety situations, choose what to do, and get **instant feedback, score, progress and a safety tip**. Scores are saved and shown on a **leaderboard**.

**Tech stack:** Java 17 · Spring Boot 3 · Spring Data JPA/Hibernate · PostgreSQL (H2 for quick demo) · React 18 (Vite)

---

## How it works

```
React UI → REST API → Controller → Service (game logic) → JPA Repository → PostgreSQL
        ←                      JSON response                          ←
```

1. Player enters a name and picks a topic (Crossing, Signals, Cycling, Bus, Pedestrian, or All).
2. For each scenario the child picks an action.
3. The backend checks the answer, awards **10 points** for a correct one, saves the attempt, and replies with feedback, the safety tip, and the new total.
4. At the end the child sees a badge (Beginner / Safe Walker / Road Safety Hero) and can open the leaderboard.

## Project structure

```
road-safety-game/
├── backend/    Spring Boot API (controller, service, repository, model, dto, exception, config)
└── frontend/   React app (Vite)
```

## Run it locally

### 1. Backend

**Option A: quick demo (no database needed, data resets on restart)**

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

**Option B: PostgreSQL (data is saved permanently)**

```sql
CREATE DATABASE roadsafety;
```

```bash
cd backend
# Defaults: localhost:5432, user postgres, password postgres. Override if different:
# export DB_URL=jdbc:postgresql://localhost:5432/roadsafety DB_USER=postgres DB_PASSWORD=yourpassword
mvn spring-boot:run
```

Tables are created automatically and **10 starter scenarios are seeded** on first run. The API runs on http://localhost:8080.

### 2. Frontend

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. In development, Vite forwards `/api` requests to the backend.

For a deployed frontend, set `VITE_API_URL` to the backend URL at build time, and set `CORS_ORIGINS` on the backend to the frontend URL.

## REST API

| Method | Endpoint | Purpose | Success |
|---|---|---|---|
| GET | `/api/scenarios?category=Cycling` | List scenarios (category optional). Correct answers are **not** included. | 200 |
| POST | `/api/players` | Start a game. Body: `{"name":"Asha"}` | 201 |
| POST | `/api/attempts` | Submit an answer. Body: `{"playerId":1,"scenarioId":2,"optionId":5}` | 201 |
| GET | `/api/players/{id}/progress` | Score, answered, correct, percent complete | 200 |
| GET | `/api/leaderboard` | Top 10 players | 200 |

**Example response of `POST /api/attempts`:**

```json
{
  "correct": true,
  "pointsEarned": 10,
  "totalScore": 30,
  "feedback": "Great job! Looking carefully and waiting for vehicles to stop keeps you safe.",
  "tip": "Stop at the edge, look right, left, and right again. Cross only when vehicles have fully stopped.",
  "correctOptionId": 2,
  "correctOptionText": "Stop, look right-left-right, and cross when vehicles have stopped"
}
```

**Errors** always look like `{"status":400,"message":"Name is required","timestamp":"..."}`

| Code | When |
|---|---|
| 400 | Missing/invalid field, bad JSON, option doesn't belong to the scenario |
| 404 | Player or scenario not found |
| 409 | Player already answered that scenario |
| 500 | Unexpected server error |

## Database design

- **players**: id, name, total_score, created_at
- **scenarios**: id, title, description, category, emoji, tip
- **answer_options**: id, option_text, correct, feedback, scenario_id → scenarios
- **attempts**: id, player_id, scenario_id, option_id, correct, points, created_at (unique per player + scenario)

## Feature checklist

- [x] Java 17, Spring Boot, React, REST APIs
- [x] JPA/Hibernate with PostgreSQL (H2 demo profile)
- [x] Validation and meaningful HTTP status codes/messages
- [x] Immediate feedback, score, progress bar, safety guidance
- [x] Leaderboard and saved progress
- [x] Responsive UI with loading, success, validation and error states
- [x] Category filter

## Upload to GitHub

```bash
cd road-safety-game
git init
git add .
git commit -m "Initial commit: Road Safety Hero (Spring Boot + React)"
git branch -M main
git remote add origin https://github.com/<your-username>/road-safety-game.git
git push -u origin main
```

## Ideas for future work

Login and roles, an admin page to add scenarios, Telugu/Hindi translations, sound effects, timed rounds.
