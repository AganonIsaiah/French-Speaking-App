package com.example.frenchlearningapp.service.generatorlogic;

/**
 * All classes only return the conjugated verb
 * Determines the present conjugation for the verbs
 *
 * Just returns the verb, no subject
 */
public class PresentConjugations {


    /**
     * Determines the group of the verb and returns the conjugated verb
     *
     * @param pn The pronoun
     * @param v The verb
     * @return The conjugated verb
     */
    public static String getPresentVerb(String pn, String v){
        StringBuilder verb = new StringBuilder(v);
        String s = " ";

        // Remove last two letters of verb
        String check = verb.substring(verb.length()-2);
        verb.delete(verb.length()-2,verb.length());

        /* Get verb */
        if (ReadCSV.isFileType(v,"irregular_verbs")[0] == 1 ) {s = Group3(pn,v, ReadCSV.isFileType(v,"irregular_verbs")[1]);}
        else if (check.equals("ir")) {s = Group2(pn,verb);}
        else if (check.equals("er")) {s = Group1(pn,verb);}

        return s;
    }



    /**
     * Conjugates verbs in the third group in present tense
     *
     * @param pn The pronoun
     * @param v The verb
     * @param indexRow The row containing the verb
     * @return The conjugated verb
     */
    private static String Group3(String pn, String v, int indexRow){

        /* Find column */
        int col = -1;
        switch (pn){
            case "je" -> {col = 2;}
            case "tu" -> {col = 3;}
            case "il","elle","on" -> {col=4;}
            case "nous" -> {col = 5;}
            case "vous" -> {col = 6;}
            case "ils","elles" -> {col = 7;}
        }

        return ReadCSV.getFileItem("irregular_verbs",indexRow,col);
    }

    /**
     * Conjugates verbs in the second group in present tense
     *
     * @param pn The pronoun (Lowercase)
     * @param verb The verb (Lowercase)
     * @return Conjugated verb
     */
    private static String Group2(String pn, StringBuilder verb){
        /* General conjugation pattern for verbs ending in ...ir */
        switch (pn) {
            case "je", "tu" -> verb.append("is");
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
     * @param verb The verb (Lowercase)
     * @return Conjugated verb
     */
    private static String Group1(String pn, StringBuilder verb){

        /* General conjugation pattern for verbs ending in ...er */
        char c = verb.charAt(verb.length()-1); // For special cases where the verb ends with ...cer or ...ger

        switch (pn) {
            case "je", "il", "elle", "on" -> verb.append('e');
            case "tu" -> verb.append("es");
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
