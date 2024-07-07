package com.example.frenchlearningapp.service.generatorlogic;

import java.util.Random;

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
    public String futureProche(String pn, String v){
        int ind = -1;
        switch (pn) {
            case "je" -> {ind = 0;}
            case "tu" -> {ind = 1;}
            case "il","elle","on" -> {ind = 2;}
            case "nous" -> {ind = 3;}
            case "vous" -> {ind = 4;}
            case "ils","elles" -> {ind = 5;}
        }

        return pn + " "+Constants.VerbsForTenses.ALLER_CON[ind] + " "+ v;
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
    public String futurePerfect(String pn, String v, int type ){

        if (type == 0){ // Avoir case
            String avoirVerb = futureSimple(pn,Constants.VerbsForTenses.FUTURE_TENSE[1]);
            String pastPart = PastConjugations.getPastParticiple(v);
            return (pn.equals("je")) ? "j'"+avoirVerb+" "+pastPart : pn+" "+avoirVerb+" "+pastPart;
        } else { // Être case --> Vandertramp verbs
            String etreVerb = futureSimple(pn,Constants.VerbsForTenses.FUTURE_TENSE[0]);
            String pastPart = v;
            // String pastPart = Objects.requireNonNull(ReadCSV.readRow("vandertramp_verbs"))[1]; // For getting past participle
            Random rand = new Random();
            char g = (rand.nextInt(2) == 0) ? 'F' : 'M';
            char q = (rand.nextInt(2) == 0) ? 'P' : 'S';

            /* Past participle verb agreement */
            //  Agreement stays the same for tu, il, vous
            switch(pn){
                case "je" -> {pastPart = Agreements.vanderTramp(pastPart,"S"+g);}
                case "elle" -> {pastPart = Agreements.vanderTramp(pastPart,"SF");}
                case "on" ->  {pastPart = Agreements.vanderTramp(pastPart,(q+g)+"");}
                case "nous" -> {pastPart = Agreements.vanderTramp(pastPart,"P"+g);}
                case "ils" -> {pastPart = Agreements.vanderTramp(pastPart,"PM");}
                case "elles" -> {pastPart = Agreements.vanderTramp(pastPart,"PF");}
            }

            return pn + " " + etreVerb +" "+pastPart;
        }

    }

    /**
     * Simple Future Verb Conjugations
     * i.e Subject + "will"...
     *
     * @param pn The pronoun
     * @param v The verb to conjugate
     * @return The verb in the Future Simple tense
     */
    public String futureSimple(String pn, String v){
        StringBuilder verb = new StringBuilder(v);

        /* Edge cases */
        int n = (v.equals ("être")) ? 0 : (v.equals("avoir")) ? 1: (v.equals("aller")) ? 2 :(v.equals("venir")) ? 3 : -1;
        verb = new StringBuilder(Constants.VerbsForTenses.FUTURE_TENSE[n]);

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
