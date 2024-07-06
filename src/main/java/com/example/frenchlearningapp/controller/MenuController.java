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
import java.util.Base64;

/**
 * Manages the generation and display of language proficiency-based audio recordings.
 */
@Controller
public class MenuController {

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
    public MenuController(SentenceGeneratorService sentenceGeneratorService, TextToSpeechService ttsService) {
        this.ttsService = ttsService;
        this.sentenceGeneratorService = sentenceGeneratorService;
    }

    /**
     * Handles GET requests to display the language proficiency selection form
     *
     * @return The index.html template
     */
    @GetMapping("/language")
    public String showLanguageForm() {
        return "index";
    }

    /**
     * Handles POST requests to generate audio based on proficiency level
     *
     * @param proficiency        User's selected proficiency
     * @param redirectAttributes Redirect attributes for flash attributes
     * @return /showRecordings page after generating audio clip
     */
    @PostMapping("/generate")
    public String generateAudio(@RequestParam String proficiency, RedirectAttributes redirectAttributes) {
        String generatedSentence = sentenceGeneratorService.generateSentence(proficiency);
        String audioFileName = "sample.mp3";
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

        /* Passes attributes to the next page */
        redirectAttributes.addFlashAttribute("proficiency", proficiency);
        redirectAttributes.addFlashAttribute("generatedSentence", generatedSentence);
        redirectAttributes.addFlashAttribute("audioFileName", audioFileName);

        return "redirect:/showRecordings";
    }

    /**
     * Handles the POST request to save the recorded audio data
     *
     * @param recordedAudio Base64-encoded string of the recorded audio data
     * @param redirectAttributes Used to pass attributes to the redirected URL
     * @return Redirects to "/showRecordings" page after saving recorded audio
     */
    @PostMapping("/saveRecording")
    public String saveRecording(@RequestParam String recordedAudio, RedirectAttributes redirectAttributes) {
        String audioDirectory = "src/main/resources/static/audio";
        String recordedFileName = "recorded.mp3";
        String recordedFilePath = audioDirectory + "/" + recordedFileName;

        Path directoryPath = Paths.get(audioDirectory);

        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Save the recorded audio file
        try {
            byte[] audioBytes = Base64.getDecoder().decode(recordedAudio.split(",")[1]);
            Files.write(Paths.get(recordedFilePath), audioBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("recordedFileName", recordedFileName);
        return "redirect:/showRecordings";
    }

    /**
     * Handles GET requests to show recording
     *
     * @param audioFileName Name of file
     * @param proficiency   User's selected proficiency level
     * @param recordedFileName For user's recorded file
     * @param model         Model to add attributes for rendering
     * @return records.html
     */
    @GetMapping("/showRecordings")
    public String showRecordings(@ModelAttribute("audioFileName") String audioFileName,
                                 @ModelAttribute("proficiency") String proficiency,
                                 @ModelAttribute("recordedFileName") String recordedFileName,
                                 Model model) {
        /* Audio files */
        String audioFile = "/audio/" + audioFileName + "?t=" + System.currentTimeMillis();
        String recordedFile = "/audio/" + recordedFileName + "?t=" + System.currentTimeMillis();

        /* Adding attributes to template */
        model.addAttribute("audioFile", audioFile);
        model.addAttribute("recordedFile", recordedFile);
        model.addAttribute("proficiency",proficiency);

        return "records";
    }


}