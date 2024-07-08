package com.example.frenchlearningapp.service.generatorlogic;

import java.util.ArrayList;
import java.util.Random;

/**
 * Accounts for gender/quantity agreements in nouns_inanimate.csv and vandertramp_csv
 */
public class Agreements {


    /**
     * Checks if there is a double vowel when connecting Strings
     *
     * @param s1 String 1
     * @param s2 String 2
     * @param flag For certain conditions, i.e when only "je" follows elision pattern
     * @return A string connecting s1 and s2 with an apostrophe or space
     */
    public static String elision(String s1, String s2, boolean flag){
        StringBuilder sb1 = new StringBuilder(s1);
        if (startsWithVowel(s2) && !startsWithVowel(String.valueOf(sb1.deleteCharAt(s1.length()-1))) && flag) return sb1.toString()+"'"+s2;
        else return s1 + " "+s2;
    }
    /**
     * For elision rule
     *
     * @param s The string
     * @return True if String starts with a vowel or h
     */
    public static boolean startsWithVowel(String s) {
        return (s.charAt(0) == 'ê' || s.charAt(0) == 'è' || s.charAt(0) == 'é' || s.charAt(0) == 'à' || s.charAt(0) == 'a' || s.charAt(0) == 'e'
                || s.charAt(0) == 'i' || s.charAt(0) == 'o'
                || s.charAt(0) == 'u' || s.charAt(0) == 'h');
    }

    /**
     * Places the correct article before the subject
     * Pattern:
     * Definite/Indefinite Articles + Animate Nouns (P)
     * Definite/Indefinite/Partitive Articles + Inanimate Nouns (L/O)
     *
     * @param s      The subject
     * @param gender 0 (Masculine), 1 (Feminine), 2 (Plural), 3 (Starts with vowel)
     * @param type   L (Lieu), O (Objet), P (Personne)
     * @return Article + Noun
     */
    public static String articleCorrection(String s, int gender, char type) {
        Random rand = new Random();

        /* For Lieu */
        if (type == 'L') {
            int prepType = rand.nextInt(3); // 0 for "à" contraction, 1 for "de" contraction, 2 for other prepositions
            String[] preposition = Constants.PREPOSITIONS;

            /* Preposition + Article + Noun pattern */
            if (prepType == 2) {
                prepType = rand.nextInt(8, 14);
                return preposition[prepType] + " " + articleCorrection(s, gender, 'P');
            }

            /* Handles à/de contractions */
            switch (gender) {
                case 0 -> {
                    return (prepType == 0) ? preposition[0] + " " + s : preposition[4] + " " + s;
                } // Masculine
                case 1 -> {
                    return (prepType == 0) ? preposition[1] + " " + s : preposition[5] + " " + s;
                } // Feminine
                case 2 -> {
                    return (prepType == 0) ? preposition[2] + " " + s : preposition[6] + " " + s;
                } // Plural
                case 3 -> {
                    return (prepType == 0) ? preposition[3] + s : preposition[7] + s;
                } // Starts with vowel
            }
        }

        /* For Objet & Personne*/
        int articleType = rand.nextInt(3); // 0 for Definite Article, 1 for Indefinite Article, 2 for Partitive Article
        if (type == 'P') articleType = rand.nextInt(2); // 0 for Definite Article, 1 for Indefinite Article

        /* Generate and add random article*/
        ArrayList<String[]> articles = new ArrayList<>();
        articles.add(Constants.Articles.DEFINITE_A);
        articles.add(Constants.Articles.INDEFINITE_A);
        articles.add(Constants.Articles.PARTITIVE_A);
        String[] a = articles.get(articleType);

        switch (gender) {
            case 0 -> {
                return a[0] + " " + s;
            }
            case 1 -> {
                return a[1] + " " + s;
            }
            case 2 -> {
                return a[2] + " " + s;
            }
            case 3 -> {
                return (a.length == 4) ? a[3] + s : a[2] + " " + s;
            }
        }
        return "";
    }

    /**
     * Adjusts the quantity/gender of a vandertramp verb for être case
     *
     * @param pastPart The past participle verb
     * @param pn The pronoun
     * @return The past participle verb, amplified with a new gender and quantity
     */
    public static String getRandVanderTramp(String pastPart, String pn){
        /* Randomizing gender and quantity */
        Random rand = new Random();
        char g = (rand.nextInt(2) == 0) ? 'F' : 'M';
        char q = (rand.nextInt(2) == 0) ? 'P' : 'S';

        /* Past participle verb agreement - tu, il, vous stays the same*/
        switch(pn){
            case "je" -> {pastPart = Agreements.etreAmplifier(pastPart,"S"+g);}
            case "elle" -> {pastPart = Agreements.etreAmplifier(pastPart,"SF");}
            case "on" ->  {pastPart = Agreements.etreAmplifier(pastPart,(q+g)+"");}
            case "nous" -> {pastPart = Agreements.etreAmplifier(pastPart,"P"+g);}
            case "ils" -> {pastPart = Agreements.etreAmplifier(pastPart,"PM");}
            case "elles" -> {pastPart = Agreements.etreAmplifier(pastPart,"PF");}
        }

        return pastPart;
    }

    /**
     * Follows agreement pattern for Dr Mrs Vandertramp verbs
     *
     * @param v    The verb (In past participle form)
     * @param type SM,SF,PM,PF
     * @return Correct form of past participle verb
     */
    public static String etreAmplifier(String v, String type) {
        return (type.equals("SF")) ? v + "e" : (type.equals("PM")) ? v + "s" : (type.equals("PF")) ? v + "es" : v;
    }

}
