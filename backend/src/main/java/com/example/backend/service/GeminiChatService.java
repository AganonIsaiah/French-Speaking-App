package com.example.backend.service;

import com.example.backend.dto.ChatRequest;
import com.example.backend.dto.TradRapidesResult;
import com.google.genai.types.GenerateContentResponse;
import com.example.backend.dto.TradRapidesCorrigeesRequest;

import com.google.genai.Client;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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

    public TradRapidesResult genTradRapidesCorrigees(TradRapidesCorrigeesRequest request) {
        String prompt = FRENCH_SYSTEM_PROMPT +
                "Voici la déclaration originale en français : " + request.getOriginalFrench() +
                "Voici la phrase traduite en anglais : " + request.getTranslatedEnglish() +
                "Sur la base de la traduction anglaise, attribuez une note sur 100 % et fournissez un court message correctif (maximum deux phrases). " +
                "Si la phrase est incorrecte, envoyez également la version traduite en anglais."+
                "Répondez sous la forme 'Points: XX; Feedback: texte'";

        GenerateContentResponse res = client.models.generateContent(
                "gemini-2.5-flash",
                prompt,
                null
        );

        String output = res.text().trim();

        int points = 0;
        String feedback = "";

        try {
            String[] parts = output.split(";", 2);
            for (String part : parts) {
                if (part.toLowerCase().contains("points")) {
                    points = Integer.parseInt(part.replaceAll("\\D+", ""));
                } else if (part.toLowerCase().contains("feedback")) {
                    feedback = part.split(":", 2)[1].trim();
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing LLM output: " + e.getMessage());
            feedback = output;
        }

        return new TradRapidesResult(points, feedback);
    }

    public List<String> genDixPhrases(String level) {
        String prompt = FRENCH_SYSTEM_PROMPT +
                 "Générer 10 phrases françaises aléatoires, avec deux phrases pour chaque niveau maximum. Numérotez toujours chacune des phrases séparées. Les générer en fonction du niveau des utilisateurs seulement :"
                + " (niveau: " + level + ")";

        GenerateContentResponse res =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null);

        return Arrays.stream(res.text().split("\\d+\\.\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty() && Character.isLetter(s.charAt(0)))
                .toList();
    }

    public String genConvo(ChatRequest chatRequest) {

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