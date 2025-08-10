package com.example.backend.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String region;
    private String level;
    private Long points;

    // Original constructor for backward compatibility
    public AuthResponse(String token, Long id, String username, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Enhanced constructor with all user data
    public AuthResponse(String token, Long id, String username, String email,
                        String region, String level, Long points) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.region = region;
        this.level = level;
        this.points = points;
    }

    // All getters and setters
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getRegion() {return region;}
    public void setRegion(String region) {this.region = region;}

    public String getLevel() {return level;}
    public void setLevel(String level) {this.level = level;}

    public Long getPoints() {return points;}
    public void setPoints(Long points) {this.points = points;}
}