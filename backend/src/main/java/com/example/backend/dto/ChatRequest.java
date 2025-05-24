package com.example.backend.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private String level;
    private String username;

    public ChatRequest() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {return level;}
    public void setLevel(String level) {this.level = level;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}
}
