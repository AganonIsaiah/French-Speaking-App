package com.example.backend.service;

import com.example.backend.dto.ChatRequest;
import com.google.genai.types.GenerateContentResponse;

import com.google.genai.Client;
import org.springframework.stereotype.Service;

@Service
public class GeminiChatService {

    private final Client client;


    public GeminiChatService(Client client) {
        this.client = client;
    }

    private static final String FRENCH_SYSTEM_PROMPT =
            "Tu es un assistant rapide en français. " +
                    "Générer les réponses avec une ponctuation correcte, ne pas ajouter d'astérisques inutiles." +
                    "Utilise un français simple et naturel. " +
                    "Répondez en deux phrases maximum.";


    public String genRes(ChatRequest chatRequest) {

        String prompt = FRENCH_SYSTEM_PROMPT +
                "\nUtilisateur: " + chatRequest.getUsername() +
                " (niveau: " + chatRequest.getLevel() + ")" +
                "\nMessage: " + chatRequest.getMessage();

        GenerateContentResponse res =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null);

        return res.text();
    }


}