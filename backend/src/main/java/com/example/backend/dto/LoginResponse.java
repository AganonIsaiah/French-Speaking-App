package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginResponse {
    private boolean success;
    private String message;
    private UserResponse user;

    public LoginResponse(){}

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponse(boolean success, String message, UserResponse user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
}
