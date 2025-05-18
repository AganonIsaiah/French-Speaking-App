package com.example.backend.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private String level;
    private String username;

    public String getMessage() {return message;}
    public String getLevel() {return level;}
    public String getUsername() {return username;}

    public void setMessage(String message) {this.message = message;}
    public void setLevel(String level) {this.level = level;}
    public void setUsername(String username) {this.username = username;}
}
