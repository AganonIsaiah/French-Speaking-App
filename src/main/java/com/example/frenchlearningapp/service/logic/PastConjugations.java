package com.example.frenchlearningapp.service.logic;

/**
 * Imparfait
 * Passé Composé
 * Passé Antérieur
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
    public String Imparfait (String pn, String v){
        StringBuilder verb = new StringBuilder(v);
        verb.delete(verb.length() - 2, verb.length()); // Remove last two letters

        switch(pn){
            case "je", "j'", "tu", "t'" -> verb.append("ais");
            case "il", "elle","on" -> verb.append("ait");
            case "nous" -> verb.append("ions");
            case "vous" -> verb.append("iez");
            case "ils", "elles" -> verb.append("aient");
        }

        return verb.toString();
    }


}
