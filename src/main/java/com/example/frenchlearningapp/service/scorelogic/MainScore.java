package com.example.frenchlearningapp.service.scorelogic;

public class MainScore {

    public static int getGrammarScore(){return 0;}
    public static int getIntonationScore(){return 0;}
    public static int getFluencyScore(){return 0;}
    public static int getPhoneticScore(){return 0;}

    public static int[] getAllScores(){
        int finalScore = (getFluencyScore()+getGrammarScore()+getPhoneticScore()+getIntonationScore()) / 4;
        return new int[] {getPhoneticScore(),getFluencyScore(),getIntonationScore(),getGrammarScore(),finalScore};
    }

    public MainScore(){
        for (int i = 0; i < getAllScores().length; i++) {
            System.out.println(getAllScores()[i]);
        }
    }
    public static void main(String[] args) {
        new MainScore();
    }
}
