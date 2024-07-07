package com.example.frenchlearningapp.service.generatorlogic;

/**
 * For regular_verbs.csv file, follows conjugation pattern for verbs in Group 1 and 2
 */
public class PresentConjugations {

    /**
     * Conjugates verbs in the second group in present tense
     *
     * @param pn The pronoun (Lowercase)
     * @param v The verb (Lowercase)
     * @return Conjugated verb
     */
    public static String Group2(String pn, String v){
        /* General conjugation pattern for verbs ending in ...ir */
        StringBuilder verb = new StringBuilder(v);
        verb.delete(verb.length() - 2, verb.length()); // Remove last two letters

        switch (pn) {

            case "je", "j'", "tu", "t'" -> verb.append("is");
            case "il", "elle","on" -> verb.append("it");
            case "nous" -> verb.append("issons");
            case "vous" -> verb.append("issez");
            case "ils", "elles" -> verb.append("issent");
        }
        return verb.toString();
    }

    /**
     * Conjugates verbs in the first group in present tense
     *
     * @param pn The pronoun (Lowercase)
     * @param v The verb (Lowercase)
     * @return Conjugated verb
     */
    public static String Group1(String pn, String v){

        // Edge case for only irregular first group verb
        if (v.equals("aller")) {
            switch (pn) {
                case "je", "j'" -> {return "vais";}
                case "tu", "t'" -> {return "vas";}
                case "il", "elle", "on" -> {return "va";}
                case "nous" -> {return "allons";}
                case "vous" -> {return "allez";}
                case "ils", "elles" -> {return "vont";}
            }
        }

        /* General conjugation pattern for verbs ending in ...er */
        StringBuilder verb = new StringBuilder(v);
        char c = verb.charAt(verb.length()-3); // For special cases where the verb ends with ...cer or ...ger
        verb.delete(verb.length() - 2, verb.length());

        switch (pn) {
            case "je", "j'", "il", "elle", "on" -> verb.append('e');
            case "tu", "t'" -> verb.append("es");
            case "nous" -> {
                if (c == 'g') verb.append('e');
                if (c == 'c') verb.replace(verb.length()-1,verb.length(),"ç");
                verb.append("ons");
            }
            case "vous" -> verb.append("ez");
            case "ils", "elles" -> verb.append("ent");
        }

        return verb.toString();
    }
}
