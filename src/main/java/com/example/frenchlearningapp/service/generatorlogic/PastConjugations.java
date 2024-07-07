package com.example.frenchlearningapp.service.generatorlogic;

/**
 * Imparfait
 * Passé Composé
 * Passé Antérieur
 * Passé Récent
 * Passé Simple
 * Plus-que-Parfait
 */
public class PastConjugations {


    /**
     * Imperfect tense, used to describe habitual actions
     *
     * @param pn Pronoun
     * @param v Verb
     * @return Conjugated verb
     */
    public String imparfait (String pn, String v){
        StringBuilder verb = new StringBuilder(v);
        verb.delete(verb.length() - 2, verb.length()); // Remove last two letters

        switch(pn){
            case "je", "tu" -> verb.append("ais");
            case "il", "elle","on" -> verb.append("ait");
            case "nous" -> verb.append("ions");
            case "vous" -> verb.append("iez");
            case "ils", "elles" -> verb.append("aient");
        }

        return verb.toString();
    }


    /**
     * Conjugates the past participle of the verb
     *
     * @param v The verb
     * @return The past participle of the verb
     */
    public static String getPastParticiple(String v) {
        /* Avoir/Etre case */
        if (v.equals("avoir")) return "eu";
        if (v.equals("être")) return "été";

        /* General case */
        StringBuilder verb = new StringBuilder(v);
        String ending = v.substring(v.length() - 2);
        verb.delete(v.length()-2,v.length());

        /* Remove last two letters, conjugate to Past Participle */
        switch (ending) {
            case "er" -> verb.append("é");
            case "ir" -> verb.append("i");
            case "re" -> verb.append("u");
        }
        return verb.toString();
    }



}
