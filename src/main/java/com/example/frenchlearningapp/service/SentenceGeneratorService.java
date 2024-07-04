package com.example.frenchlearningapp.service;

import org.springframework.stereotype.Service;

/**
 * Pattern Structures
 *
 * A1: Subject + verb + noun
 */
@Service
public class SentenceGeneratorService {



    public String generateSentence(String proficiencyLevel){
        return "J'ai mangé des pommes.";
    }


}
