package com.example.frenchlearningapp.service;

import org.springframework.stereotype.Service;

/**
 * Manages the scoring by performing analysis on the user's recorded mp3 file
 */
@Service
public class ScoreService {

    private Long sampleLength, recordedLength;
    

    public void setLengths(Long sample, Long recorded){
        this.sampleLength = sample;
        this.recordedLength = recorded;
    }

    /**
     * Based on length difference between the sample.mp3 and recorded.mp3 files
     * Based on recorded.mp3 transcript vs generated sentence
     * @return
     */
    public int calcFluencyScore(){
        return 0;
    }

    /**
     * Based on recored.mp3 transcript vs generated sentence
     * @return
     */
    public int calcGrammarScore(){
        return -1;
    }

    public ScoreService(){

    }



    /**
     * Index access:
     * 0 - Phonetic Score -  
     * 1 - Fluency Score - Speed of recorded.mp3 vs sample.mp3
     * 2 - Intonation Score
     * 3 - Grammar Score - Transcription of the recorded.mp3 file, points lost based on how different transcription is to sentence generated
     * 4 - Total Score
     *
     * @return Array with each score
     */
    // public int[] generateScore(){
    //     return MainScore.getAllScores();
    // }
}
