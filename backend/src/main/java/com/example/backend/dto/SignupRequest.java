package com.example.backend.dto;

public class SignupRequest {
    private String username;
    private String password;
    private String email;

    private String region;
    private String level;

    public SignupRequest() {
    }

    public SignupRequest(String username, String password, String email, String region, String level) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.region = region;
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRegion() {
        return region;
    }

    public String getLevel() {
        return level;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}