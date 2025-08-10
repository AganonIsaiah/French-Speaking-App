package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.genai.Client;

@Configuration
public class GeminiConfig {

    @Bean
    public Client geminiClient() {
        return new Client.Builder()
                .apiKey("AIzaSyBDrJN67ou2Wffbw1LO8pa4ikDxWz1huiw").build();
    }
}
