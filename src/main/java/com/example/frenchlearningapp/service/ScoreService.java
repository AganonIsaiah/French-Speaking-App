package com.example.frenchlearningapp.service;

import com.example.frenchlearningapp.service.scorelogic.MainScore;
import org.springframework.stereotype.Service;

/**
 * Manages the scoring by performing analysis on the user's recorded mp3 file
 */
@Service
public class ScoreService {

    /**
     * Index access:
     * 0 - Phonetic Score
     * 1 - Fluency Score
     * 2 - Intonation Score
     * 3 - Grammar Score
     * 4 - Total Score
     *
     * @return Array with each score
     */
    public int[] generateScore(){
        return MainScore.getAllScores();
    }
}
