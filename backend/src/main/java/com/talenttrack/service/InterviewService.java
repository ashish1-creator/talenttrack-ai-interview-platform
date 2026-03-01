package com.talenttrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talenttrack.model.InterviewEvaluationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${groq.api.key}")
    private String apiKey;

    public InterviewService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api.groq.com/openai/v1")
                .build();
    }

    // ===============================
    // Allowed roles
    // ===============================
    private final List<String> allowedRoles = List.of(
            "java developer",
            "frontend developer",
            "backend developer",
            "full stack developer",
            "python developer",
            "data analyst",
            "devops engineer",
            "react developer",
            "angular developer",
            "software engineer",
            "java full stack developer",
            "python full stack developer",
            "mern stack developer",
            "cyber security"
    );

    // ===============================
    // Allowed levels
    // ===============================
    private final List<String> allowedLevels = List.of(
            "beginner",
            "intermediate",
            "advanced"
    );

    // ==========================================================
    // 1️⃣ GENERATE QUESTIONS
    // ==========================================================
    public List<String> generateQuestions(String role, String level) {

        validateRoleAndLevel(role, level);

        String prompt = "Generate exactly 10 interview theory questions for a "
                + role + " at " + level
                + " level. Return only numbered questions without explanations.";

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are an interview question generator."),
                Map.of("role", "user", "content", prompt)
        );

        Map<String, Object> requestBody = buildRequest(messages, 0.7, 800);

        Map response = callGroq(requestBody);

        List<Map<String, Object>> choices =
                (List<Map<String, Object>>) response.get("choices");

        if (choices == null || choices.isEmpty()) {
            return List.of("No questions generated.");
        }

        String content = ((Map<String, Object>) choices.get(0).get("message"))
                .get("content").toString();

        return Arrays.stream(content.split("\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(line -> line.replaceAll("^\\d+\\.\\s*", ""))
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 2️⃣ EVALUATE FULL INTERVIEW (Single Final Feedback)
    // ==========================================================
    public InterviewEvaluationResponse evaluateInterview(
            String role,
            String level,
            List<String> questions,
            List<String> answers) throws Exception {

        validateRoleAndLevel(role, level);

        StringBuilder qaBuilder = new StringBuilder();

        for (int i = 0; i < questions.size(); i++) {
            qaBuilder.append("Question ").append(i + 1).append(": ")
                    .append(questions.get(i)).append("\n");
            qaBuilder.append("Answer ").append(i + 1).append(": ")
                    .append(answers.get(i)).append("\n\n");
        }

        String prompt = """
                You are a senior technical interviewer.

                Evaluate this full mock interview for a %s at %s level.

                %s

                Provide response strictly in JSON format:
                {
                  "overallScore": number (0-10),
                  "strengths": "...",
                  "weakAreas": "...",
                  "finalFeedback": "...",
                  "recommendation": "..."
                }
                """.formatted(role, level, qaBuilder.toString());

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "You are an expert technical interviewer."),
                Map.of("role", "user", "content", prompt)
        );

        Map<String, Object> requestBody = buildRequest(messages, 0.2, 1000);

        Map response = callGroq(requestBody);

        List<Map<String, Object>> choices =
                (List<Map<String, Object>>) response.get("choices");

        String content = ((Map<String, Object>) choices.get(0).get("message"))
                .get("content").toString()
                .replace("```json", "")
                .replace("```", "")
                .trim();

        Map<String, Object> result =
                objectMapper.readValue(content, Map.class);

        return new InterviewEvaluationResponse(
                (int) result.get("overallScore"),
                result.get("strengths").toString(),
                result.get("weakAreas").toString(),
                result.get("finalFeedback").toString(),
                result.get("recommendation").toString()
        );
    }

    // ==========================================================
    // COMMON METHODS
    // ==========================================================

    private void validateRoleAndLevel(String role, String level) {

        if (role == null || level == null) {
            throw new IllegalArgumentException("Role and Level must not be null");
        }

        role = role.toLowerCase().trim();
        level = level.toLowerCase().trim();

        if (!allowedRoles.contains(role)) {
            throw new IllegalArgumentException("Invalid role selected: " + role);
        }

        if (!allowedLevels.contains(level)) {
            throw new IllegalArgumentException("Invalid level selected: " + level);
        }
    }

    private Map<String, Object> buildRequest(List<Map<String, String>> messages,
                                             double temperature,
                                             int maxTokens) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.3-70b-versatile");
        requestBody.put("messages", messages);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);

        return requestBody;
    }

    private Map callGroq(Map<String, Object> requestBody) {

        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Groq API Error: " + body))
                )
                .bodyToMono(Map.class)
                .block();
    }
}