import { useState } from "react";
import { submitAnswer } from "../api";

export default function GameScreen({ player, scenarios, onFinish }) {
  const [index, setIndex] = useState(0);
  const [chosenId, setChosenId] = useState(null);
  const [feedback, setFeedback] = useState(null); // response from POST /api/attempts
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");
  const [score, setScore] = useState(0);
  const [correctCount, setCorrectCount] = useState(0);

  const scenario = scenarios[index];
  const isLast = index === scenarios.length - 1;
  const progress = Math.round(((index + (feedback ? 1 : 0)) / scenarios.length) * 100);

  async function choose(option) {
    if (feedback || busy) return;
    setBusy(true);
    setError("");
    setChosenId(option.id);
    try {
      const res = await submitAnswer(player.id, scenario.id, option.id);
      setFeedback(res);
      setScore(res.totalScore);
      if (res.correct) setCorrectCount((c) => c + 1);
    } catch (e) {
      setError(e.message);
      setChosenId(null);
    } finally {
      setBusy(false);
    }
  }

  function next() {
    if (isLast) {
      onFinish({ score, correct: correctCount, total: scenarios.length });
      return;
    }
    setIndex((i) => i + 1);
    setFeedback(null);
    setChosenId(null);
  }

  function optionClass(option) {
    if (!feedback) return "option";
    if (option.id === feedback.correctOptionId) return "option correct";
    if (option.id === chosenId) return "option wrong";
    return "option dim";
  }

  return (
    <div className="card">
      <div className="topbar">
        <span>Scenario {index + 1} of {scenarios.length}</span>
        <span className="score">⭐ {score}</span>
      </div>
      <div
        className="progress"
        role="progressbar"
        aria-valuenow={progress}
        aria-valuemin={0}
        aria-valuemax={100}
      >
        <div className="progress-fill" style={{ width: `${progress}%` }} />
      </div>

      <div className="emoji">{scenario.emoji}</div>
      <h2>{scenario.title}</h2>
      <p className="description">{scenario.description}</p>

      <div className="options">
        {scenario.options.map((o) => (
          <button
            key={o.id}
            className={optionClass(o)}
            disabled={!!feedback || busy}
            onClick={() => choose(o)}
          >
            {o.text}
          </button>
        ))}
      </div>

      {busy && <p className="muted" role="status">Checking your answer…</p>}
      {error && <p className="error" role="alert">{error}</p>}

      {feedback && (
        <div className={`feedback ${feedback.correct ? "good" : "bad"}`} role="status">
          <h3>{feedback.correct ? "✅ Correct! +" + feedback.pointsEarned + " points" : "❌ Not quite!"}</h3>
          <p>{feedback.feedback}</p>
          {!feedback.correct && (
            <p><strong>Best choice:</strong> {feedback.correctOptionText}</p>
          )}
          <p className="tip">💡 <strong>Safety tip:</strong> {feedback.tip}</p>
          <button className="btn big" onClick={next}>
            {isLast ? "See my results 🏁" : "Next scenario →"}
          </button>
        </div>
      )}
    </div>
  );
}
