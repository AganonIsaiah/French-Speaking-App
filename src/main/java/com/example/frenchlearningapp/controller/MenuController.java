package com.example.frenchlearningapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.frenchlearningapp.service.ScoreService;
import com.example.frenchlearningapp.service.SentenceGeneratorService;
import com.example.frenchlearningapp.service.TextToSpeechService;

/**
 * Manages the generation and display of language proficiency-based audio recordings.
 */
@Controller
public class MenuController {

    // Holds instance of SentenceGeneratorService and TextToSpeechService
    private final SentenceGeneratorService sentenceGeneratorService;
    private final TextToSpeechService ttsService;
    private final ScoreService scoreService;
    private String sentence;
    private Long sampleDuration;

    /**
     * Injects SentenceGeneratorService dependency.
     *
     * @param sentenceGeneratorService For generating written sentences
     * @param ttsService               For generating sound recordings
     */
    @Autowired
    public MenuController(SentenceGeneratorService sentenceGeneratorService, TextToSpeechService ttsService, ScoreService scoreService) {
        this.ttsService = ttsService;
        this.sentenceGeneratorService = sentenceGeneratorService;
        this.scoreService = scoreService;
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
        Long sampleDuration = ttsService.generateSpeech(generatedSentence, audioFilePath);
        this.sampleDuration = sampleDuration;

        /* Passes attributes to the next page */
        redirectAttributes.addFlashAttribute("proficiency", proficiency);
        redirectAttributes.addFlashAttribute("generatedSentence", generatedSentence);
        redirectAttributes.addFlashAttribute("audioFileName", audioFileName);
        redirectAttributes.addFlashAttribute("sampleDuration",sampleDuration);

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
    public String saveRecording(@RequestParam String recordedAudio, @RequestParam String recordingDuration, RedirectAttributes redirectAttributes) {
        String audioDirectory = "src/main/resources/static/audio";
        String recordedFileName = "recorded.mp3";
        String recordedFilePath = audioDirectory + "/" + recordedFileName;
    
        Path directoryPath = Paths.get(audioDirectory);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Failed to create directory");
                return "redirect:/showRecordings";
            }
        }
    
        // Save the recorded audio file
        try {
            String base64Audio = recordedAudio.split(",")[1];
            byte[] audioBytes = Base64.getDecoder().decode(base64Audio);
            Files.write(Paths.get(recordedFilePath), audioBytes);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to save recorded audio");
            return "redirect:/showRecordings";
        }

        String generatedSentence = (String) redirectAttributes.getFlashAttributes().get("generatedSentence");

        Long recordedDuration = Long.parseLong(recordingDuration);
        System.out.println("recorded.mp3 file duration: "+recordedDuration+ " milliseconds");

        redirectAttributes.addFlashAttribute("recordedFileName", recordedFileName);
        redirectAttributes.addFlashAttribute("generatedSentence", generatedSentence);  
        redirectAttributes.addFlashAttribute("recordedDuration", recordedDuration);     
        redirectAttributes.addFlashAttribute("recordedFileName", recordedFileName);
        return "redirect:/analysis";
    }

     /* 
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
                                 @ModelAttribute("generatedSentence") String generatedSentence, 
                                 @ModelAttribute("sampleDuration") Long sampleDuration,
                                 Model model) {
        /* Audio files */
        String audioFile = "/audio/" + audioFileName + "?t=" + System.currentTimeMillis();
        String recordedFile = "/audio/" + recordedFileName + "?t=" + System.currentTimeMillis();

        /* Adding attributes to template */
        model.addAttribute("audioFile", audioFile);
        model.addAttribute("recordedFile", recordedFile);
        model.addAttribute("proficiency",proficiency);
        model.addAttribute("generatedSentence", generatedSentence); 
        model.addAttribute("sampleDuration", sampleDuration);

        this.sentence = generatedSentence;
        return "records";
    }
    
    /**
     * Handles GET requests to show the analysis page
     *
     * @param model Model to add attributes for rendering
     * @return analysis.html
     * @throws Exception 
     */
    @GetMapping("/analysis")
    public String showAnalysis(  Model model) throws Exception {
        /* Set attributes on page */
        String sampleFilePath = (String) model.asMap().get("sampleFilePath");
        String recordedFilePath = (String) model.asMap().get("recordedFilePath");
        Long recordedDuration = (Long)model.asMap().get("recordedDuration");
        
        /* Get scores, x/100 %*/
        scoreService.set(sampleDuration, recordedDuration, sentence);
        String transcription = scoreService.getTranscript();
        double grammarScore = scoreService.calcGrammarScore()*100;
        double fluencyScore = scoreService.calcFluencyScore()*100;
    
        
        double totalScore = scoreService.calcTotalScore()*100;
    
        /* Testing */
        System.out.println("\n******************\nFluency Score: "+fluencyScore+
        "\nGrammar Score: "+grammarScore
        +"\nTotal Score: "+totalScore+"\n******************\n");

        /* To model */
        model.addAttribute("generatedSentence", sentence);
        model.addAttribute("userTranscript",transcription);
        model.addAttribute("sampleFilePath", sampleFilePath);
        model.addAttribute("recordedFilePath", recordedFilePath);
        // Add Scores
        model.addAttribute("fluencyScore",fluencyScore);
        model.addAttribute("grammarScore",grammarScore);


        model.addAttribute("totalScore",totalScore);




        return "analysis";
    }


}