package com.example.frenchlearningapp.service.generatorlogic;

import java.util.Objects;
import java.util.Random;

/**
 * NOTE - Conjugations are good, connections need work
 */
public class MainGenerator {

    private static Random rand = new Random();


    /**
     * Randomizes a subject
     *
     * @return Subject
     */
    private static String getSubject(){
        rand = new Random();
        int n = rand.nextInt(9); // 0 - Je, 1 - Tu, 2,3,4 - Il/Elle/On, 5 - Nous, 6 - Vous, 7,8 - Ils/Elles
        return Constants.Pronouns.PRONOUNS[n];
    }

    /**
     * Conjugates the verb based on the subject
     *
     * @param subject The subject
     * @param tense The tense for conjugation
     * @return New sentence with Subject + Verb
     */
    private static String getVerb(String subject, String tense) {
        /* Generate a verb */
        rand = new Random();
        int n = rand.nextInt(3);
        String fileName = (n == 0) ? "irregular_verbs" : "regular_verbs"; // 0 for irregular_verbs, 1,2 for regular_verbs
        String infinitiveVerb = Objects.requireNonNull(ReadCSV.readFile(fileName))[0]; // Verb from irregular/regular

        /* For tenses which require Avoir/Etre auxiliary verb */
        String vandertrampVerb = Objects.requireNonNull(ReadCSV.readFile("vandertramp_verbs"))[0];
        int typeInt = rand.nextInt(2);
        String verbType = (typeInt == 0) ? infinitiveVerb : vandertrampVerb; // 0 for avoir, 1 for être

        if (tense.equals("present")) {
            /* Get verb */
            String conjugateVerb = PresentConjugations.getPresentVerb(subject, infinitiveVerb);
            return Agreements.elision(subject, conjugateVerb,subject.equals("je"));
        } else if (tense.equals("future")){
            switch (rand.nextInt(3)) {
                case 0 -> {return FutureConjugations.futureSimple(subject,infinitiveVerb);}
                case 1 -> {return FutureConjugations.futureProche(subject,infinitiveVerb);}
                case 2 -> {return FutureConjugations.futureAnterieur(subject,verbType,typeInt);}
            }
        } else if (tense.equals("past")){
            switch (rand.nextInt(6)) {
                case 0 -> {return PastConjugations.imparfait(subject,infinitiveVerb);}
                case 1 -> {return PastConjugations.passeRecent(subject,infinitiveVerb);}

                case 2 -> {return PastConjugations.passeCompose(subject,verbType,typeInt);}
                case 3 -> {return PastConjugations.plusQueParfait(subject,verbType,typeInt);}

                /* Written past tenses */
                case 4 -> {return PastConjugations.passeSimple(subject,infinitiveVerb);}
                case 5 -> {return PastConjugations.passeAnterieur(subject,verbType,typeInt);}
            }
        }
        return " ";
    }
    private static String getAdverb(){return "";}

    /**
     * Adjectives agree with noun (In gender and quantity)
     *
     * Most Follow This Pattern: Article + Noun + Adjective
     * B.A.N.G.S (Beauty/Age/Number/Goodness/Size Adjectives): Article + Adjective + Noun
     *
     * @param typeNoun Type of Noun L (Lieu), O (Objet), P (Personne)
     * @param s The sentence with the Noun
     * @param gender Adjective must agree with Noun
     * @return Sentence with adjectives
     */
    private static String getAdjectives(char typeNoun, String s, char gender){
         rand = new Random();
        String[] readCsv = ReadCSV.readFile("adjectives");
        assert readCsv != null;


        // Before a noun: Article + Adjective + Noun
        if (readCsv[1].equals("B")){

        }

        // After a noun: Article + Noun + Adjective
        else {


        }
        return "";
    }

    /**
     * Generates a noun with respect to its article
     *
     * @return A string containing an Article and Noun, and the Gender
     */
    private static String[] getNouns() {
        rand = new Random();

        /* Generate a noun from the animate/inanimate files */
        int genre = rand.nextInt(2); // 0 for inanimate, 1 for animate
        String fileName = (genre == 0) ? "nouns_inanimate" : "nouns_animate";
        String[] readCsv = ReadCSV.readFile(fileName);
        assert readCsv != null;

        // System.out.print(fileName + ": ");
        /* Correct the noun with the proper agreement */
        if (genre == 0) {
            String noun = readCsv[0];
            char type = readCsv[1].charAt(0);
            char gender = readCsv[2].charAt(0);
            int genderN = (gender == 'M') ? 0 : (gender == 'F') ? 1 : 2;

            // Article + Object
            // Preposition + Article + Place
            if (Agreements.startsWithVowel(noun) && gender != 2) genderN = 3;
            return new String[] {Agreements.articleCorrection(noun, genderN, type), String.valueOf(gender)};
        } else {
            // Article + Subject
            int col = rand.nextInt(2, 5); // For animate objects - 2 for masculine, 3 for feminine, 4 for plural
            String noun = readCsv[col];
            int genderN = col - 2;

            String gender = (genderN == 0) ? "M" : (genderN == 1) ? "F" : (genderN == 2) ? "P" : "V";
            if (Agreements.startsWithVowel(noun) && genderN != 2) genderN = 3;

            return new String[] {Agreements.articleCorrection(readCsv[col], genderN, 'P'),gender};
        }
    }

    public static String getSentence(String proficiency){
        return " ";
    }
    public MainGenerator(String proficiency) {
        for (int i = 0; i < 10; i++) {
            int n = rand.nextInt(3);
            String tense = (n == 0) ? "present" : (n == 1) ? "past" : "future";
            System.out.println("Tense: " + tense + "; "+getVerb(getSubject(),tense));
        }
    }

    public static void main(String[] args) {
        new MainGenerator("A1");
    }
}
