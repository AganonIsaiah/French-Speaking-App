package com.example.backend.dto;

public class AuthResponse {
    private String jwt_token;
    private String message;
    private UserDTO user;

    public AuthResponse(String jwt_token, String message, UserDTO user) {
        this.jwt_token = jwt_token;
        this.message = message;
        this.user = user;
    }

    public String getJwt_token() {
        return jwt_token;
    }

    public void setJwt_token(String jwt_token) {
        this.jwt_token = jwt_token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public static class UserDTO {
        private String username;
        private String email;
        private String region;
        private String level;

        public UserDTO(String username, String email, String region, String level) {
            this.username = username;
            this.email = email;
            this.region = region;
            this.level = level;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }

        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }

    }
}
