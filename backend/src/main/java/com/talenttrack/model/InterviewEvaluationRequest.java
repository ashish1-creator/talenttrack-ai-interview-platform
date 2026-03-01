package com.talenttrack.model;

import java.util.List;

public class InterviewEvaluationRequest {

    private String role;
    private String level;
    private List<String> questions;
    private List<String> answers;

    // ===== Getters =====

    public String getRole() {
        return role;
    }

    public String getLevel() {
        return level;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public List<String> getAnswers() {
        return answers;
    }

    // ===== Setters =====

    public void setRole(String role) {
        this.role = role;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}