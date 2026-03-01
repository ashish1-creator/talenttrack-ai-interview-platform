import "./App.css";
import { useState } from "react";
import { generateQuestions } from "./services/api";
import axios from "axios";

function App() {
  const [role, setRole] = useState("");
  const [level, setLevel] = useState("");
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState([]);
  const [evaluation, setEvaluation] = useState(null);
  const [loading, setLoading] = useState(false);

  const roles = [
    "Java Developer",
    "Frontend Developer",
    "Backend Developer",
    "Full Stack Developer",
    "Python Developer",
    "Data Analyst",
    "DevOps Engineer",
    "React Developer",
    "Angular Developer",
    "Software Engineer",
    "Java Full Stack Developer",
    "Python Full Stack Developer",
    "MERN Stack Developer",
    "Cyber Security"
  ];

  const levels = ["Beginner", "Intermediate", "Advanced"];

  const handleStart = async () => {
    if (!role || !level) return;

    try {
      setLoading(true);
      const data = await generateQuestions(role, level);
      setQuestions(data);
      setAnswers(new Array(data.length).fill(""));
    } catch (error) {
      alert("Failed to generate questions.");
    } finally {
      setLoading(false);
    }
  };

  const handleAnswerChange = (index, value) => {
    const updated = [...answers];
    updated[index] = value;
    setAnswers(updated);
  };

  const handleSubmit = async () => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/interview/evaluate-interview",
        {
          role,
          level,
          questions,
          answers
        }
      );

      setEvaluation(response.data);
    } catch (error) {
      alert("Evaluation failed.");
    }
  };

  return (
    <div className="app">
      <div className="card">

        {!questions.length && (
          <>
            <h1>TalentTrack</h1>
            <p>AI Powered Mock Interview Platform</p>

            <select
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="input"
            >
              <option value="">Select Role</option>
              {roles.map((r, i) => (
                <option key={i}>{r}</option>
              ))}
            </select>

            <select
              value={level}
              onChange={(e) => setLevel(e.target.value)}
              className="input"
            >
              <option value="">Select Level</option>
              {levels.map((l, i) => (
                <option key={i}>{l}</option>
              ))}
            </select>

            <button
              className="primary-btn"
              onClick={handleStart}
              disabled={!role || !level || loading}
            >
              {loading ? "Generating..." : "Start Interview"}
            </button>
          </>
        )}

        {questions.length > 0 && !evaluation && (
          <>
            <h1>Interview Questions</h1>

            {questions.map((q, index) => (
              <div key={index} style={{ marginBottom: "20px", textAlign: "left" }}>
                <p><strong>{index + 1}. {q}</strong></p>
                <textarea
                  className="input"
                  rows="3"
                  placeholder="Type your answer here..."
                  value={answers[index]}
                  onChange={(e) =>
                    handleAnswerChange(index, e.target.value)
                  }
                />
              </div>
            ))}

            <button className="primary-btn" onClick={handleSubmit}>
              Submit Interview
            </button>
          </>
        )}

        {evaluation && (
          <>
            <h1>Interview Evaluation</h1>
            <p><strong>Overall Score:</strong> {evaluation.overallScore}</p>
            <p><strong>Strengths:</strong> {evaluation.strengths}</p>
            <p><strong>Weak Areas:</strong> {evaluation.weakAreas}</p>
            <p><strong>Final Feedback:</strong> {evaluation.finalFeedback}</p>
            <p><strong>Recommendation:</strong> {evaluation.recommendation}</p>
          </>
        )}

      </div>
    </div>
  );
}

export default App;