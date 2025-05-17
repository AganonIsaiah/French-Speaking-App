package com.example.backend.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private int points;
    private String proficiency;
    private String region;
}
