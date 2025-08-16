package com.example.backend.service;

import com.example.backend.dto.ChatRequest;
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



    public String genTradRapidesCorrigees(TradRapidesCorrigeesRequest request) {
        String prompt = FRENCH_SYSTEM_PROMPT +
                "Voici la déclaration originale en français :"+ request.getOriginalFrench()
                +"Voici la phrase traduite en anglais :" + request.getTranslatedEnglish()
                +"Sur la base de la traduction anglaise, attribuez une note sur 100 %. Ajoutez au maximum deux phrases d'analyse si nécessaire.";

        GenerateContentResponse res =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null);

        return res.text();
    }


    public List<String> genDixPhrases(String level) {
        String prompt = FRENCH_SYSTEM_PROMPT +
                 "Générer 10 phrases françaises aléatoires, avec deux phrases pour chaque niveau maximum. Les générer en fonction du niveau des utilisateurs seulement :"
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