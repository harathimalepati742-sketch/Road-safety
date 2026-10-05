import { useEffect, useState } from "react";
import { getScenarios, createPlayer } from "./api";
import StartScreen from "./components/StartScreen";
import GameScreen from "./components/GameScreen";
import ResultScreen from "./components/ResultScreen";
import Leaderboard from "./components/Leaderboard";

export default function App() {
  const [screen, setScreen] = useState("start"); // start | play | result | leaderboard
  const [scenarios, setScenarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [player, setPlayer] = useState(null);
  const [queue, setQueue] = useState([]);
  const [result, setResult] = useState(null);

  function loadScenarios() {
    setLoading(true);
    setError("");
    getScenarios()
      .then(setScenarios)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }

  useEffect(loadScenarios, []);

  const categories = [...new Set(scenarios.map((s) => s.category))];

  // Throws on failure so StartScreen can show the error.
  async function startGame(name, category) {
    const newPlayer = await createPlayer(name);
    const chosen =
      category === "All" ? scenarios : scenarios.filter((s) => s.category === category);
    setPlayer(newPlayer);
    setQueue(chosen);
    setScreen("play");
  }

  function finishGame(summary) {
    setResult(summary);
    setScreen("result");
  }

  return (
    <div className="app">
      <header className="header">
        <h1>🚦 Road Safety Hero</h1>
        <p>Learn how to stay safe on the road!</p>
      </header>

      {loading && (
        <div className="card center" role="status">
          <div className="spinner" />
          <p>Loading scenarios…</p>
        </div>
      )}

      {!loading && error && (
        <div className="card center">
          <p className="error" role="alert">{error}</p>
          <button className="btn" onClick={loadScenarios}>Try again</button>
        </div>
      )}

      {!loading && !error && screen === "start" && (
        <StartScreen
          categories={categories}
          onStart={startGame}
          onLeaderboard={() => setScreen("leaderboard")}
        />
      )}

      {!loading && !error && screen === "play" && (
        <GameScreen player={player} scenarios={queue} onFinish={finishGame} />
      )}

      {!loading && !error && screen === "result" && (
        <ResultScreen
          player={player}
          result={result}
          onPlayAgain={() => setScreen("start")}
          onLeaderboard={() => setScreen("leaderboard")}
        />
      )}

      {!loading && !error && screen === "leaderboard" && (
        <Leaderboard onBack={() => setScreen("start")} />
      )}
    </div>
  );
}
