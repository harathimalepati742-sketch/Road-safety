import { useEffect, useState } from "react";
import { getLeaderboard } from "../api";

export default function Leaderboard({ onBack }) {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  function load() {
    setLoading(true);
    setError("");
    getLeaderboard()
      .then(setRows)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }

  useEffect(load, []);

  const medals = ["🥇", "🥈", "🥉"];

  return (
    <div className="card">
      <h2>🏆 Top Players</h2>

      {loading && <p className="muted" role="status">Loading…</p>}

      {error && (
        <>
          <p className="error" role="alert">{error}</p>
          <button className="btn" onClick={load}>Try again</button>
        </>
      )}

      {!loading && !error && rows.length === 0 && (
        <p className="muted">No scores yet. Be the first to play!</p>
      )}

      {rows.length > 0 && (
        <ol className="board">
          {rows.map((r) => (
            <li key={r.rank}>
              <span>{medals[r.rank - 1] || r.rank}</span>
              <span className="name">{r.name}</span>
              <span className="pts">{r.score} pts</span>
            </li>
          ))}
        </ol>
      )}

      <button className="btn ghost" onClick={onBack}>← Back</button>
    </div>
  );
}
