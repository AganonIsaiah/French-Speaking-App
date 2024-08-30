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
    private double grammarScore;

    @Autowired
    private TranscriptionService transcriptionService;

    /**
     * Based on length difference between the sample.mp3 and recorded.mp3 files
     * Based on recorded.mp3 transcript vs generated sentence
     * @return Fluency Score out of 100%
     */
    public double calcFluencyScore(){
        int sample = sampleLength.intValue();
        int recorded = recordedLength.intValue();
        double score = 0;

        if (sample + 1000 > recorded && sample - 1000 < recorded) score = 0.15;
        else if (sample + 500 > recorded && sample - 500 < recorded) score = 0.30;
        else if (sample + 200 > recorded && sample - 200 < recorded) score = 0.5;
        else if (score + 100 > recorded && sample - 100 < recorded) score = 0.70;
        else if (score + 50 > recorded && sample - 50 < recorded) score = 0.90;
        else if (score + 25 > recorded && sample - 25 < recorded) score = 1.00;
        else return 0.0;

        return (score+grammarScore)/2;
    }

    /**
     * Based on recored.mp3 transcript vs generated sentence
     * @return Grammar score out of 100%
     */
    public double calcGrammarScore(){
        HashMap<Integer,String> genMap = getMapString(genSentence);
        HashMap<Integer, String> transcriptMap = getMapString(transcription);
        double totalScore = genMap.size();
        double newScore = 0;


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

        System.out.println("\n******************\nSETTERS for SCORESERVICE\nSample: "+sampleLength+"\nRecorded: "+recordedLength+"\nSentence: "+genSentence+"\nTranscript: "+transcription+"\n******************\n");
    }


}
