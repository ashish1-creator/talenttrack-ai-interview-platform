package com.talenttrack.model;

public class InterviewEvaluationResponse {

    private int overallScore;
    private String strengths;
    private String weakAreas;
    private String finalFeedback;
    private String recommendation;

    public InterviewEvaluationResponse() {}

    public InterviewEvaluationResponse(int overallScore,
                                       String strengths,
                                       String weakAreas,
                                       String finalFeedback,
                                       String recommendation) {
        this.overallScore = overallScore;
        this.strengths = strengths;
        this.weakAreas = weakAreas;
        this.finalFeedback = finalFeedback;
        this.recommendation = recommendation;
    }

    public int getOverallScore() { return overallScore; }
    public void setOverallScore(int overallScore) { this.overallScore = overallScore; }

    public String getStrengths() { return strengths; }
    public void setStrengths(String strengths) { this.strengths = strengths; }

    public String getWeakAreas() { return weakAreas; }
    public void setWeakAreas(String weakAreas) { this.weakAreas = weakAreas; }

    public String getFinalFeedback() { return finalFeedback; }
    public void setFinalFeedback(String finalFeedback) { this.finalFeedback = finalFeedback; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
}