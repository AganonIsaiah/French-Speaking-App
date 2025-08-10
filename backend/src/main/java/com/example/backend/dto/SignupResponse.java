package com.example.backend.dto;

public class SignupResponse {
    private String message;
    private String username;

    public SignupResponse() {}

    public SignupResponse(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
