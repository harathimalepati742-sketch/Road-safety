// All calls to the Spring Boot backend live here.
// In dev, Vite proxies /api to localhost:8080. For production set VITE_API_URL.
const BASE = import.meta.env.VITE_API_URL || "";

async function request(path, options = {}) {
  let res;
  try {
    res = await fetch(BASE + path, {
      headers: { "Content-Type": "application/json" },
      ...options,
    });
  } catch {
    throw new Error("Cannot reach the server. Is the backend running?");
  }
  const data = await res.json().catch(() => null);
  if (!res.ok) {
    throw new Error(data?.message || `Request failed (${res.status})`);
  }
  return data;
}

export const getScenarios = () => request("/api/scenarios");

export const createPlayer = (name) =>
  request("/api/players", { method: "POST", body: JSON.stringify({ name }) });

export const submitAnswer = (playerId, scenarioId, optionId) =>
  request("/api/attempts", {
    method: "POST",
    body: JSON.stringify({ playerId, scenarioId, optionId }),
  });

export const getProgress = (playerId) => request(`/api/players/${playerId}/progress`);

export const getLeaderboard = () => request("/api/leaderboard");
