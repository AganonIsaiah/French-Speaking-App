package com.example.frenchlearningapp.controller;

import com.example.frenchlearningapp.service.SentenceGeneratorService;
import com.example.frenchlearningapp.service.TextToSpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Controller
public class LevelController {

    // Holds instance of SentenceGeneratorService and TextToSpeechService
    private final SentenceGeneratorService sentenceGeneratorService;
    private final TextToSpeechService ttsService;

    /**
     * Injects SentenceGeneratorService dependency.
     *
     * @param sentenceGeneratorService For generating written sentences
     * @param ttsService               For generating sound recordings
     */
    @Autowired
    public LevelController(SentenceGeneratorService sentenceGeneratorService, TextToSpeechService ttsService) {
        this.ttsService = ttsService;
        this.sentenceGeneratorService = sentenceGeneratorService;
    }

    /**
     * Displays language proficiency selection form
     *
     * @return The index.html template
     */
    @GetMapping("/language")
    public String showLanguageForm() {
        return "index";
    }


    @PostMapping("/generate")
    public String generateAudio(@RequestParam String proficiency, RedirectAttributes redirectAttributes) {
        String generatedSentence = sentenceGeneratorService.generateSentence(proficiency);
        String audioFileName = "output.mp3";
        String audioDirectory = "src/main/resources/static/audio";
        String audioFilePath = audioDirectory + "/" + audioFileName;

        // Ensure the directory exists, create if not
        Path directoryPath = Paths.get(audioDirectory);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Generate the audio file
        ttsService.generateSpeech(generatedSentence, audioFilePath);

        // Add attributes to redirect
        redirectAttributes.addFlashAttribute("proficiency", proficiency);
        redirectAttributes.addFlashAttribute("generatedSentence", generatedSentence);
        redirectAttributes.addFlashAttribute("audioFileName", audioFileName);

        return "redirect:/showRecordings";

    }


    @GetMapping("/showRecordings")
    public String showRecordings(@ModelAttribute("audioFileName") String audioFileName, Model model) {

        String audioFile = "/audio/" + audioFileName + "?t=" + System.currentTimeMillis();
        model.addAttribute("audioFile", audioFile);

        return "records";
    }


}