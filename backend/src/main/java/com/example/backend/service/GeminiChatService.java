package com.example.backend.service;

import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.ResponseStream;

import org.springframework.beans.factory.annotation.Value;
import com.google.genai.Client;
import org.springframework.stereotype.Service;


@Service
public class GeminiChatService {

    private final Client chatClient;
    private static final String FRENCH_SYSTEM_PROMPT =
            "Tu es un assistant rapide en français. " +
                    "Si l'utilisateur fait une erreur grammaticale, corrigez-le et expliquez-lui succinctement pourquoi. Ignorer la casse et les signes de ponctuation manquants" +
                    "Utilise un français simple et naturel. ";


    @Value("${gemini.model:gemini-2.5-flash}")
    private String modelName;

    private final GenerateContentConfig config = GenerateContentConfig.builder()
            .temperature(0.0f)
            .topP(0.7f)
            .topK(10.0f)
            .build();

    public GeminiChatService(Client chatClient) {
        this.chatClient = chatClient;
    }

    public String genRes(String prompt) {

        String optimizedPrompt = FRENCH_SYSTEM_PROMPT + "\n\nUtilisateur: " + prompt;


        ResponseStream<GenerateContentResponse> responseStream =
                chatClient.models.generateContentStream(
                        modelName,
                        optimizedPrompt,
                        config);

        StringBuilder response = new StringBuilder();
        for (GenerateContentResponse res : responseStream) {
            response.append(res.text());
        }

        responseStream.close();

        return response.toString();


    }


}