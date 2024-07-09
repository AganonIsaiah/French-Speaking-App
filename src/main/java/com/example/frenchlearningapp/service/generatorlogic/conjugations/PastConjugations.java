package com.example.frenchlearningapp.service.generatorlogic.conjugations;

import com.example.frenchlearningapp.service.generatorlogic.Agreements;
import com.example.frenchlearningapp.service.generatorlogic.ReadCSV;

/**
 * Methods return the Subject + Conjugated Verb(s)
 *
 * Imparfait (Subject + [Verb - Last two letters + endings])
 * Passé Composé (Subject + [Avoir/Être (present conjugation)] + verb/vandertramp past participle verb)
 * Passé Récent (Subject + venir [present conjugation] + de + verb (infinitive))
 * Plus-que-Parfait (Subject + [Avoir/Être (imparfait conjugation)] + verb/vandertramp past participle verb)
 *
 * Used in written context
 * Passé Simple (Subject + Verb (passé simplé conjugation))
 * Passé Antérieur (Subject + [Avoir/Être (passé  simplé conjugation)] + verb/vandertramp past participle verb)
 */
public class PastConjugations {


    /**
     * Passé Antérieur, used to describe an action that was completed before another action
     * Usually in literature/historical text, rarely used in spoken French
     * i.e Subject + "had/were" + past participle
     *
     * @param pn The pronoun
     * @param v The verb
     * @param type 0 for avoir, 1 for etre
     * @return Subject + Auxiliary Verb [Être/Avoir] (passé simplé conjugation) + past participle of verb
     */
    public static String passeAnterieur(String pn, String v, int type){
        /* Get passe simple form of avoir/etre */
        String auxVerb = passeSimple(pn,type == 0 ? "avoir" : "être");
        String pastPart = getPastParticiple(v, (type == 0 && ReadCSV.isFileType(v,"vandertramp_verbs")[0] == 0) ? "past participle" : "vandertramp");

        if (type == 1) { // Être case --> Vandertramp verbs
            pastPart = Agreements.getRandVanderTramp(pastPart,pn);
        }

        return auxVerb +" "+pastPart;
    }

    /**
     * Passé Simplé, used to describe actions that were completed in the past
     * Usually in written text, rarely in spoken French
     *
     * @param pn The pronoun
     * @param v The Verb
     * @return The subject and verb (passé simplé conjugation)
     */
    public static String passeSimple(String pn, String v){
        StringBuilder verb = new StringBuilder(v);
        String lastTwo = verb.substring(verb.length()-2); // Get the last two letters

        if (lastTwo.equals("er")) {
            char c = verb.charAt(verb.length()-1); // Get the third last letter
            verb.delete(verb.length()-2,verb.length()); // Delete the last two letters
            // If er verb and ends with cer or ger (for all except ils/elles))
            if (c == 'c' || c == 'g') switch (pn) {case "je","tu","il","elle","on","nous","vous" -> {
                if ((c == 'c')) verb.append("ç");
                else verb.append("e");
                }}

            switch (pn) {
                case "je" -> verb.append("ai");
                case "tu" -> verb.append("as");
                case "il","elle","on" -> verb.append("a");
                case "nous" -> verb.append("âmes");
                case "vous" -> verb.append("âtes");
                case "ils","elles" -> verb.append("èrent"); // If er verb change to è (for ils/elles)
            }
        } else {
            char c = verb.charAt(verb.length()-2); // Get the second last letter
            verb.deleteCharAt(verb.length()-1); // Delete the last letter
            int []check = ReadCSV.isFileType(v,"irregular_verbs"); // Traverses irregular verbs

            /* For irregular verb stems */
            if (check[0] == 1) {
                verb = new StringBuilder(ReadCSV.getIrregularStem(v,"passe simple"));
                c = verb.charAt(verb.length()-1);
            }

            switch (pn) {
                case "nous","vous" -> {
                    verb.deleteCharAt(verb.length()-1); // Delete the last letter and replace with a circumflexe
                    if (c == 'i') verb.append("î");
                    if (c == 'u') verb.append("û");
                }
            }

            switch (pn) {
                case "je", "tu" -> verb.append("s");
                case "il","elle","on" -> verb.append("t");
                case "nous" -> verb.append("mes");
                case "vous" -> verb.append("tes");
                case "ils","elles" -> verb.append("rent"); // If ir verb change to i (for ils/elles)
            }
        }


        return Agreements.elision(pn,verb.toString(),pn.equals("je"));
    }

    /**
     * Plus-que-parfait, used to describe actions that have been completed before another past action
     * i.e Subject + "had" + verb
     * 3. Can be formed with reflexive verbs
     *
     * @param pn The pronoun
     * @param v The verb
     * @param type 0 for Avoir, 1 for Être
     * @return Subject + Avoir/Etre (Imparfait) + Past Participle
     */
    public static String plusQueParfait(String pn, String v, int type){

        // Turns infinitive verb into its part participle
        String pastPart = getPastParticiple(v, ( type == 0 && ReadCSV.isFileType(v,"vandertramp_verbs")[0] == 0) ? "past participle" : "vandertramp");
        String auxVerb = imparfait(pn, (type == 0) ? "avoir" : "être");

        if (type == 1) { // Être case --> Vandertramp verbs
            pastPart = Agreements.getRandVanderTramp(pastPart,pn);
        }

        return auxVerb +" "+pastPart;
    }

    /**
     * Passé Récent, used to describe actions that have just happened
     * i.e Subject + "just" + verb
     *
     * @param pn The pronoun
     * @param v The verb
     * @return Subject + venir (present conjugation) + de + verb (infinitive)
     */
    public static String passeRecent(String pn, String v){
        return Agreements.elision(pn, PresentConjugations.getPresentVerb(pn,"venir"),pn.equals("je")) + " "+Agreements.elision("de",v,true);
    }

    /**
     * Passé Composé, used to describe completed actions
     * i.e Subject + "have" + past participle
     *
     * @param pn The pronoun
     * @param v The verb
     * @param type 0 for avoir, 1 for être
     * @return Subject + Avoir/Etre (Present) + Past Participle
     */
    public static String passeCompose(String pn, String v, int type){
        // Turns infinitive verb into its part participle
        String pastPart = PastConjugations.getPastParticiple(v, (type == 0 && ReadCSV.isFileType(v,"vandertramp_verbs")[0] == 0) ? "past participle" : "vandertramp");
        String auxVerb = PresentConjugations.getPresentVerb(pn,(type == 0) ? "avoir" : "être");

        if (type == 1) { // Être case --> Vandertramp verbs
            pastPart = Agreements.getRandVanderTramp(pastPart,pn);
        }

        return Agreements.elision(pn,auxVerb,pn.equals("je")) +" "+pastPart;
    }

    /**
     * Imperfect tense, used to describe habitual/ongoing actions in the past
     * i.e Subject + "was/were"
     *
     * @param pn Pronoun
     * @param v Verb
     * @return Subject + Conjugated verb
     */
    public static String imparfait (String pn, String v){
        StringBuilder verb = new StringBuilder(v);
        int []check = ReadCSV.isFileType(v,"irregular_verbs"); // For getting the irregular stems

        char c = verb.charAt(verb.length()-1); // Get third last letter
        String lastTwo = verb.substring(verb.length()-2); // Get the last two letters
        verb.delete(verb.length() - 2, verb.length()); // Remove last two letters

        /* For irregular verb stems */
        if (check[0] == 1) verb = new StringBuilder(ReadCSV.getIrregularStem(v,"imparfait"));

        /* Endings for cer and ger, opposite of regular present case, nous/vous not effected */
        switch (pn){
            case "je","tu","il","elle","on","ils","elles" -> {
                if (c == 'c') verb.append("ç");
                if (c == 'g') verb.append("e");
            }
        }

        /* Case for second group verbs, same ending, different lower stems -iss */
        if (lastTwo.equals("ir") && check[0] == 0) verb.append("iss"); // If verb is 2nd group and not the irregular stem

        switch(pn){
            case "je", "tu" -> verb.append("ais");
            case "il", "elle","on" -> verb.append("ait");
            case "nous" -> verb.append("ions");
            case "vous" -> verb.append("iez");
            case "ils", "elles" -> verb.append("aient");
        }

        return Agreements.elision(pn,verb.toString(), pn.equals("je"));
    }

    /**
     * Conjugates the past participle of the verb (Handles Irregular Case)
     *
     * @param v The verb
     * @param fileType vandertramp_verbs.csv of irregular_verbs.csv
     * @return The past participle of the verb
     */
    public static String getPastParticiple(String v, String fileType) {

        /* Irregular case */
        String checkIrregular = ReadCSV.getIrregularStem(v,fileType);
        if (!checkIrregular.equals("flag")) return checkIrregular;

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
