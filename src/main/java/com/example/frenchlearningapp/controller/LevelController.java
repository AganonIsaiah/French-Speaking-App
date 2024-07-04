package com.example.frenchlearningapp.controller;

import com.example.frenchlearningapp.service.SentenceGeneratorService;
import com.example.frenchlearningapp.service.TextToSpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LevelController {

    // Holds instance of SentenceGeneratorService and TextToSpeechService
    private final SentenceGeneratorService sentenceGeneratorService;
    private final TextToSpeechService ttsService;

    /**
     * Injects SentenceGeneratorService dependency.
     *
     * @param sentenceGeneratorService For generating written sentences
     * @param ttsService For generating sound recordings
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

    /**
     * Redirects the user to the showRecordings page - which includes the generated sentence,
     * text-to-speech recording, and record button
     *
     * @param proficiency Hold's the user's proficiency level
     * @param model Model to add attributes
     *
     * @return The records.html template
     */
    @PostMapping("/language")
    public String showRecordings(@RequestParam String proficiency, Model model){
        /* Preparing sentence and audio */
        String generatedSentence =  sentenceGeneratorService.generateSentence(proficiency);  // Get sentence based on proficiency using the injected service instance
        String audioFile = "audio/output.mp3"; // Where the audio file will be saved

        // Generates text-to-speech and saves audio file
        ttsService.generateSpeech(generatedSentence, audioFile);

        /* Saves user's proficiency level, the generated sentence, and audio file */
        model.addAttribute("proficiency", proficiency);
        model.addAttribute("generatedSentence", generatedSentence );
        model.addAttribute("audioFile",audioFile);

        System.out.println("Level: " + proficiency + "\nSentence: " + generatedSentence);

        return "records";
    }
}