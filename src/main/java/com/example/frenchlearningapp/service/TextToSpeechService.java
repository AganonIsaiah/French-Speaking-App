package com.example.frenchlearningapp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.stereotype.Service;

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
     * @return Length, in milliseconds, of sample.mp3 file
     */
    public Long generateSpeech(String text, String outputFile) {
        /* From api.voicerss */
        String apiKey = "491c7457e821467d8d4ef98b5a450268";
        String languageCode = "fr-fr"; // fr-ca French Canada, fr-fr French France
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


                    // Get duration
                    Long duration = getAudioDurationInMillis(outputFile);
                    System.out.println("sample.mp3 file duration: " + duration + " milliseconds");

                    return duration;
                }
            } else {
                System.out.println("VoiceRSS API Error: " + connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (long) -1;
    }

      /**
     * Retrieves the duration of the audio file in milliseconds
     *
     * @param filePath Path to the audio file
     * @return Duration of the audio in milliseconds
     */
    public Long getAudioDurationInMillis(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(audioFile);
            long microseconds = (long) ((fileFormat.getFrameLength() / fileFormat.getFormat().getFrameRate()) * 1_000_000);
            return microseconds / 1_000; // Convert to milliseconds
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            return (long) -1;
        }
    }
}
