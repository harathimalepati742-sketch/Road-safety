function getBadge(percent) {
  if (percent >= 80) return { icon: "🏆", title: "Road Safety Hero!", note: "Amazing! You know how to stay safe." };
  if (percent >= 50) return { icon: "🚸", title: "Safe Walker", note: "Good job! A little more practice and you'll be a hero." };
  return { icon: "🐣", title: "Beginner", note: "Keep learning. Read the safety tips and try again!" };
}

export default function ResultScreen({ player, result, onPlayAgain, onLeaderboard }) {
  const percent = result.total === 0 ? 0 : Math.round((result.correct / result.total) * 100);
  const badge = getBadge(percent);

  return (
    <div className="card center">
      <div className="emoji big">{badge.icon}</div>
      <h2>{badge.title}</h2>
      <p>{badge.note}</p>

      <div className="stats">
        <div><strong>{player.name}</strong><span>Player</span></div>
        <div><strong>{result.score}</strong><span>Score</span></div>
        <div><strong>{result.correct}/{result.total}</strong><span>Correct</span></div>
      </div>

      <button className="btn big" onClick={onPlayAgain}>Play again 🔁</button>
      <button className="btn ghost" onClick={onLeaderboard}>🏆 Leaderboard</button>
    </div>
  );
}
