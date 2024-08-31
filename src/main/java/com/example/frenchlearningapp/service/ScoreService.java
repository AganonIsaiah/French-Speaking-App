package com.example.frenchlearningapp.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Manages the scoring by performing analysis on the user's recorded mp3 file
 */
@Service
public class ScoreService {

    private Long sampleLength;
    private Long recordedLength;
    private String genSentence;
    private String transcription;
    private static final String filePath = "src/main/resources/static/audio/recorded.mp3";
    private double grammarScore, fluencyScore;

    @Autowired
    private TranscriptionService transcriptionService;

    /** CALCULATING SCORES METHODS **/
    /**
     * Based on length difference between the sample.mp3 and recorded.mp3 files
     * Based on recorded.mp3 transcript vs generated sentence
     * @return Fluency Score out of 100%
     */
    public double calcFluencyScore(){
        int sample = sampleLength.intValue();
        int recorded = recordedLength.intValue();
        int difference = sample - recorded;
        double score = 0.00;


        if (difference < 1000) score = 0.15;
        if (difference < 750) score = 0.25;
        if (difference < 500) score = 0.5;
        if (difference < 250) score = 0.65;
        if (difference < 100) score = 0.7;
        if (difference < -175) score = 0.85;
        if (difference < -250) score = 0.9;
        if (difference < -500) score = 1.00;

        System.out.println("Fluency Score: "+score+"\tDifference: "+difference);

        this.fluencyScore = (score+grammarScore)/2;
        return fluencyScore;
    }

    /**
     * Based on recored.mp3 transcript vs generated sentence
     * @return Grammar score out of 100%
     */
    public double calcGrammarScore(){
        HashMap<Integer,String> genMap = getMapString(genSentence);
        HashMap<Integer, String> transcriptMap = getMapString(transcription);
        double totalScore = genMap.size();
        double newScore = 0.00;


        for (int i = 0; i < totalScore; i++) {
            if (genMap.containsKey(i) && transcriptMap.containsKey(i)) {
                if (genMap.get(i).equals(transcriptMap.get(i))) {
                    newScore++;
                }
            }
        }

        System.out.println("\nGenerated Map: "+genMap+"\nTranscription Map: "+transcriptMap+ "\ntotalScore: "+totalScore + "\nnewScore "+newScore);

        this.grammarScore = newScore/totalScore;
        return grammarScore;
    }


    public double calcTotalScore(){
        return (grammarScore+fluencyScore)/2;
    }

    /** HELPER METHODS **/
    /**
     * @return The user's transcribed speech
     */
    public String getTranscript(){return transcription;}

    /**
     * For calculating grammar
     * @param sentence The transcript to pass
     * @return Map of words seperated by white space
     */
    private HashMap<Integer,String> getMapString(String sentence){
        int index = 0;
        HashMap<Integer,String> map = new HashMap<>(); // 1. The index, 2. The String, seperated by the white space
        StringBuilder sb = new StringBuilder();

        for (int i =0; i < sentence.length(); i++){ 
            char c = sentence.charAt(i);

            if (c == ' ') {
                if (sb.length() > 0) { // Only put in map if sb is not empty
                    map.put(index, sb.toString());
                    sb.setLength(0); // Clear the StringBuilder
                    index++;
                }
            } else  sb.append(c);
            
        }
        // Add the last word if there is any remaining in the StringBuilder
        if (sb.length() > 0) map.put(index, sb.toString()); 
        
        return map;
    }

    /**
     * Each scores out of 100
     * 0 - Phonetic Score -  
     * 1 - Fluency Score - Speed of recorded.mp3 vs sample.mp3
     * 2 - Intonation Score
     * 3 - Grammar Score - Transcription of the recorded.mp3 file, points lost based on how different transcription is to sentence generated
     * 4 - Total Score
     * 
     * 
     * @param sampleLength sample.mp3 duration in milliseconds
     * @param recoredLength recorded.np3 duration in milliseconds
     * @param genSentence The generated French sentence
     * @throws Exception 
     */
    public void set (Long sampleLength, Long recordedLength, String genSentence) throws Exception{
        this.sampleLength = sampleLength;
        this.recordedLength = recordedLength;
        this.genSentence = genSentence;
        this.transcription = transcriptionService.transcribe(filePath);
   }


}
