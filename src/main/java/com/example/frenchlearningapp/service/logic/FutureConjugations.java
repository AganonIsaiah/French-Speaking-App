package com.example.frenchlearningapp.service.logic;

/**
 * Futur
 * Futur Antérieur
 * Futur Simple
 */
public class FutureConjugations {

    public FutureConjugations(){

        for (int i = 0; i < Constants.Pronouns.PRONOUNS.length   ; i++)
            System.out.println(FutureSimple(Constants.Pronouns.PRONOUNS[i],"finir"));
    }

    public static void main(String[] args) {
        new FutureConjugations();
    }
    public String FutureSimple(String pn, String v){
        StringBuilder verb = new StringBuilder(v);

        /* Same verb ending for all groups, remove last letter from ...re verbs */
        if (verb.charAt(verb.length()-1) == 'e') verb.deleteCharAt(verb.length()-1);
        switch(pn){
            case "je", "j'" -> verb.append("ai");
            case "tu", "t'" -> verb.append("as");
            case "il", "elle","on" -> verb.append("a");
            case "nous" -> verb.append("ons");
            case "vous" -> verb.append("ez");
            case "ils", "elles" -> verb.append("ont");
        }

        return verb.toString();
    }
}
