package com.example.backend.dto;

public class TradRapidesResult {
    private int points;
    private String feedback;

    public TradRapidesResult() {}

    public TradRapidesResult(int points, String feedback) {
        this.points = points;
        this.feedback = feedback;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
