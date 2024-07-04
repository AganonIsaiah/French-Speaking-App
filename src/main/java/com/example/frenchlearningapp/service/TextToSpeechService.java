package com.example.frenchlearningapp.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Utilizes Text-to-Speech to create a sound recording of the written sentence
 */
@Service
public class TextToSpeechService {

    /**
     * Generates audio clip based on text-based French sentence
     *
     * @param text Sentence from SentenceGeneratorService class
     * @param outputFile Path for outputFile
     */
    public void generateSpeech(String text, String outputFile) {
        /* From api.voicerss */
        String apiKey = "491c7457e821467d8d4ef98b5a450268";
        String languageCode = "fr-ca"; // French (Canada) language code
        String voiceRssEndpoint = "http://api.voicerss.org/";

        try {
            // Encode text for URL
            String encodedText = URLEncoder.encode(text, "UTF-8");

            // Create URL
            URL url = new URL(voiceRssEndpoint + "?key=" + apiKey + "&hl=" + languageCode + "&src=" + encodedText);

            // Create connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Get response
            int responseCode = connection.getResponseCode();
            System.out.println("VoiceRSS API Response Code: " + responseCode);

            // Read response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Audio file saved to: " + outputFile);
                }
            } else {
                System.out.println("VoiceRSS API Error: " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
