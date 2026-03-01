import axios from "axios";

const API_BASE = "http://localhost:8080/api/interview";

export const generateQuestions = async (role, level) => {
  const response = await axios.post(`${API_BASE}/generate`, {
    role,
    level,
  });

  return response.data;
};