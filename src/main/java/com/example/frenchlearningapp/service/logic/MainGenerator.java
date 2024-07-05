package com.example.frenchlearningapp.service.logic;

import java.util.Random;

/**
 * A1: SVO
 */
public class MainGenerator {

    private String level;





    public MainGenerator(String proficiency){
        this.level = proficiency;
        Random rand = new Random();
        int subj = rand.nextInt(10); // 0 - 9

    }

    public static void main(String[] args) {
        new MainGenerator("A1");
    }
}
