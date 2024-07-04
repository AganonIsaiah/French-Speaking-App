package com.example.frenchlearningapp.controller;

import com.example.frenchlearningapp.service.SentenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LevelController {

    // Holds instance of SentenceGeneratorService
    private final SentenceGeneratorService sentenceGeneratorService;

    /**
     * Injects SentenceGeneratorService dependency.
     *
     * @param sentenceGeneratorService For generating sentences
     */
    @Autowired
    public LevelController(SentenceGeneratorService sentenceGeneratorService) {
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
        // Get sentence based on proficiency using the injected service instance
        String generatedSentence =  sentenceGeneratorService.generateSentence(proficiency);

        // Saves user's proficiency level and the generated sentence
        model.addAttribute("proficiency", proficiency);
        model.addAttribute("generatedSentence", generatedSentence );

        System.out.println("Level: " + proficiency + "\nSentence: " + generatedSentence );

        return "records";
    }




}