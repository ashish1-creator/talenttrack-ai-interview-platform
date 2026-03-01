import "./App.css";
import { useState } from "react";
import { generateQuestions } from "./services/api";
import axios from "axios";

const API_BASE = "https://talenttrack-ai-interview-platform.onrender.com/api/interview";

function App() {
  const [role, setRole] = useState("");
  const [level, setLevel] = useState("");
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState([]);
  const [evaluation, setEvaluation] = useState(null);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

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
    if (!role || !level) {
      alert("Please select role and level.");
      return;
    }

    try {
      setLoading(true);
      setEvaluation(null);
      const data = await generateQuestions(role, level);
      setQuestions(data);
      setAnswers(new Array(data.length).fill(""));
    } catch (error) {
      console.error(error);
      alert("Failed to generate questions. Please try again.");
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
    const hasEmpty = answers.some(ans => ans.trim() === "");

    if (hasEmpty) {
      alert("Please answer all questions before submitting.");
      return;
    }

    try {
      setSubmitting(true);

      const response = await axios.post(
        `${API_BASE}/evaluate-interview`,
        {
          role,
          level,
          questions,
          answers
        }
      );

      setEvaluation(response.data);

    } catch (error) {
      console.error(error);
      alert("Evaluation failed. Please try again.");
    } finally {
      setSubmitting(false);
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
              {loading ? "Generating Questions..." : "Start Interview"}
            </button>

            {loading && (
              <p style={{ marginTop: "10px" }}>
                Please wait, AI is generating your questions...
              </p>
            )}
          </>
        )}
        {questions.length > 0 && !evaluation && (
          <>
            <h1>Interview Questions</h1>

            {questions.map((q, index) => (
              <div
                key={index}
                style={{ marginBottom: "20px", textAlign: "left" }}
              >
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

            <button
              className="primary-btn"
              onClick={handleSubmit}
              disabled={submitting}
            >
              {submitting ? "Evaluating..." : "Submit Interview"}
            </button>

            {submitting && (
              <p style={{ marginTop: "10px" }}>
                AI is evaluating your answers, please wait...
              </p>
            )}
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