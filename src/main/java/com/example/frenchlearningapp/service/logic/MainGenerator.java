package com.example.frenchlearningapp.service.logic;

import java.util.HashMap;
import java.util.Random;

/**
 * A1: SVO
 */
public class MainGenerator {

    private String level;

    private String[] sampleVerbs = {"manger","parler","aller","ajouter","apporter","gagner","penser","garder","visiter","donner","acheter"};

    HashMap<Character,String> nounMap = new HashMap<>(); // Character --> gender: M,F,P
                                                         // String --> Noun

    Random rand = new Random();
    PresentConjugations pc = new PresentConjugations();

    // Char listing
    /*
        S = Subject
        V = Verb
        A = Article
        N = Noun
     */


    // Je vais à l'ecole
    // Je mange les pommes
    // Subject + verb + article + noun
    // Article + adjective + noun + verb: gender of noun affects article and adjectives
    // Article + noun + être + adjective

    private String LevelA1(StringBuilder sb, char nextType, String currentWord){
        if (nextType == 'V') {

                LevelA1(sb.append(currentWord+" "),'A',pc.Group1(currentWord,sampleVerbs[rand.nextInt(sampleVerbs.length)]));
        }
        if (nextType == 'A')
            LevelA1(sb.append(currentWord+" "),'N',Constants.DEFINITE_A[rand.nextInt(2)]);
        if (nextType == 'N'){
            sb.append(currentWord+" ");

            switch(currentWord){
                case "le" -> sb.append(nounMap.get('M'));
                case "la" -> sb.append(nounMap.get('F'));
            }
        }

        return sb.toString()+".";
    }

    public MainGenerator(String proficiency){
        this.level = proficiency;

        nounMap.put('M',"livre");
        nounMap.put('M',"stylo");
        nounMap.put('M',"chat");

        nounMap.put('F',"table");
        nounMap.put('F',"chaise");
        nounMap.put('F',"voiture");


        for (int i = 0; i < 6; i++) {
            this.rand = new Random();
            StringBuilder sb = new StringBuilder();
            System.out.println("Sentence "+i+": "+LevelA1(sb,'V',Constants.PRONOUNS[rand.nextInt(11)]));
        }
    }

    public static void main(String[] args) {
        new MainGenerator("A1");
    }
}
