import axios from "axios";

const API_BASE = "https://talenttrack-ai-interview-platform.onrender.com/api/interview";

export const generateQuestions = async (role, level) => {
  const response = await axios.post(`${API_BASE}/generate`, {
    role,
    level,
  });

  return response.data;
};
