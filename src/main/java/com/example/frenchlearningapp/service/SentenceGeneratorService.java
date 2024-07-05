package com.example.frenchlearningapp.service;

import org.springframework.stereotype.Service;

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

        String test1 = "Est-ce que vous avez un stylo pour moi, s'il vous plaît?";
        String test2 = "J'ai mangé des pommes.";
        String test3 = "Je vais à l'école, aujourd'hui.";

        return test3 + " "+test1;
    }

}
