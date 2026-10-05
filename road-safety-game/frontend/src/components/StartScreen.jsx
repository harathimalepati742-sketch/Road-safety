import { useState } from "react";

export default function StartScreen({ categories, onStart, onLeaderboard }) {
  const [name, setName] = useState("");
  const [category, setCategory] = useState("All");
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    if (!name.trim()) {
      setError("Please enter your name to start.");
      return;
    }
    setBusy(true);
    setError("");
    try {
      await onStart(name.trim(), category);
    } catch (err) {
      setError(err.message);
      setBusy(false);
    }
  }

  return (
    <form className="card" onSubmit={handleSubmit} noValidate>
      <h2>Ready to play?</h2>

      <label htmlFor="name">Your name</label>
      <input
        id="name"
        value={name}
        maxLength={30}
        placeholder="Type your name"
        onChange={(e) => setName(e.target.value)}
        aria-invalid={!!error}
      />

      <label>Pick a topic</label>
      <div className="chips">
        {["All", ...categories].map((c) => (
          <button
            type="button"
            key={c}
            className={`chip ${category === c ? "active" : ""}`}
            onClick={() => setCategory(c)}
          >
            {c}
          </button>
        ))}
      </div>

      {error && <p className="error" role="alert">{error}</p>}

      <button className="btn big" type="submit" disabled={busy}>
        {busy ? "Starting…" : "Start Game 🚀"}
      </button>
      <button className="btn ghost" type="button" onClick={onLeaderboard}>
        🏆 View Leaderboard
      </button>
    </form>
  );
}
