package com.example.frenchlearningapp.service;

import org.springframework.stereotype.Service;

import com.example.frenchlearningapp.service.generatorlogic.MainGenerator;

/**
 * Utilizes logic within the "logic" package to construct appropriate and grammatically sound French sentences
 */
@Service
public class SentenceGeneratorService {

    /**
     * Crafts a written French sentence
     *
     * @param proficiencyLevel Used as a scalar for sentence generation
     * @return French sentence
     */
    public String generateSentence(String proficiencyLevel){
        String s = MainGenerator.getSentence(proficiencyLevel);
       // s = "je vais à l'école!";
        return s;
    }

}
