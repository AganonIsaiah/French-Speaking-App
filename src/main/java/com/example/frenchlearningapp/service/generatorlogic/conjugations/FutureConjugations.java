package com.example.frenchlearningapp.service.generatorlogic.conjugations;

import com.example.frenchlearningapp.service.generatorlogic.Agreements;
import com.example.frenchlearningapp.service.generatorlogic.ReadCSV;

/**
 * Methods return the Subject + Conjugated Verb(s)
 *
 * Futur Proche (Subject + Aller (Present Conjugation) + Verb (Infinitive))
 * Futur Antérieur (Subject + Future Tense of Avoir/Être --> aur/ser [with futur simple endings] + Past Participle of Main verb)
 * Futur Simple (Subject + Verb (Remove last two letter and Add "avoir" endings)
 */
public class FutureConjugations {

    /**
     * Near Future Verb Conjugations
     * i.e Subject + "going to"...
     *
     * @param pn The Pronoun
     * @param v The Verb
     * @return Subject + Aller (Conjugated) + Verb (Infinitive)
     */
    public static String futureProche(String pn, String v){
        return pn + " "+ PresentConjugations.getPresentVerb(pn,"aller") + " "+ v;
    }

    /**
     * Perfect Future Verb Conjugations (Completion of an action before another future action)
     * i.e Subject + "will have"...
     *
     * @param pn The pronoun
     * @param v The verb to be conjugated to past participle
     * @param type 0 for Avoir, 1 for Être
     * @return Future Perfect Tense of Avoir/Être + Past Participle of verb
     */
    public static String futureAnterieur(String pn, String v, int type ){
        // Turns infinitive verb into its part participle
        String pastPart = PastConjugations.getPastParticiple(v, (type == 0  && ReadCSV.isFileType(v,"vandertramp_verbs")[0] == 0) ? "past participle" : "vandertramp");
        String verb = futureSimple(pn, (type == 0) ? "avoir" : "être");

        if (type == 1) { // Être case --> Vandertramp verbs
            pastPart = Agreements.getRandVanderTramp(pastPart,pn);
        }

        return verb +" "+pastPart;
    }

    /**
     * Simple Future Verb Conjugations
     * i.e Subject + "will"...
     *
     * @param pn The pronoun
     * @param v The verb to conjugate
     * @return The verb in the Future Simple tense
     */
    public static String futureSimple(String pn, String v){
        StringBuilder verb = new StringBuilder(v);
        int []checkRow = ReadCSV.isFileType(v,"irregular_verbs");

        /* Edge cases */
        if (checkRow[0] == 1)  verb = new StringBuilder(ReadCSV.getFileItem("irregular_verbs",checkRow[1],8));

        /* Same verb ending for all groups, remove last letter from ...re verbs */
        if (verb.charAt(verb.length()-1) == 'e') verb.deleteCharAt(verb.length()-1);

        // (Verb - ending) + avoir ending conjugation pattern
        switch(pn){
            case "je" -> verb.append("ai");
            case "tu" -> verb.append("as");
            case "il", "elle","on" -> verb.append("a");
            case "nous" -> verb.append("ons");
            case "vous" -> verb.append("ez");
            case "ils", "elles" -> verb.append("ont");
        }

        // Elision rule only for je
        return Agreements.elision(pn,verb.toString(),pn.equals("je"));
    }


}
