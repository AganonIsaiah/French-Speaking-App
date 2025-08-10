package com.example.backend.service;

import com.google.genai.types.GenerateContentResponse;

import lombok.RequiredArgsConstructor;
import com.google.genai.Client;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiChatService {

    private final Client client;



    private static final String FRENCH_SYSTEM_PROMPT =
            "Tu es un assistant rapide en français. " +
                    "Générer les réponses avec une ponctuation correcte, ne pas ajouter d'astérisques inutiles." +
                    "Utilise un français simple et naturel. ";


    public String genRes(String message) {

        String prompt = FRENCH_SYSTEM_PROMPT + "\n" + message;
        GenerateContentResponse res =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        message,
                        null);

        return res.text();
    }


}