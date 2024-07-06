package com.example.frenchlearningapp.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Clears the cache for audio generation, ensuring the correct audio file is created.
 */
@RestController
public class AudioController {

    /**
     * Retrieves and serves the requested audio file.
     *
     * @param audioFileName The audio file to retrieve
     * @return ResponseEntity containing the audio file bytes
     */
    @GetMapping("/audio/{audioFileName}")
    public ResponseEntity<byte[]> getAudioFile(@PathVariable String audioFileName) {
        String audioDirectory = "src/main/resources/static/audio";
        String audioFilePath = audioDirectory + "/" + audioFileName;

        // Read the audio file bytes
        Path path = Paths.get(audioFilePath);
        byte[] audioBytes;
        try {
            audioBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        // Set cache control headers
        CacheControl cacheControl = CacheControl.maxAge(0, TimeUnit.SECONDS).mustRevalidate();
        return ResponseEntity.ok().cacheControl(cacheControl).body(audioBytes);
    }
}