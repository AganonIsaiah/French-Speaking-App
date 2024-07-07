package com.example.frenchlearningapp.service.generatorlogic;


import java.util.Random;

public class MainGenerator {

    private static Random rand = new Random();

    /**
     * S + V + A + O
     * Present Tense
     */
    private static String simpleSentence(){
        return getVerb(getSubject(),"present") + " " +getNouns()[0]+".";
    }

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
    private static String getVerb(String subject, String tense){
        rand = new Random();

        /* Generate a noun from the animate/inanimate files */
        int genre = rand.nextInt(2); // 0 for regular_verbs, 1 for irregular_verbs
        String fileName = (genre == 0) ? "regular_verbs" : "irregular_verbs";
        String[] readCsv = ReadCSV.readRow(fileName);
        assert readCsv != null;
        String conjugateVerb;

        /* Conjugates verb */
        if (genre == 0){ // Verbs that follow the regular pattern
            String verb = readCsv[0];
            conjugateVerb = (verb.substring(verb.length()-2,verb.length()-1).equals("ir")) ? PresentConjugations.Group2(subject, verb) : PresentConjugations.Group1(subject, verb);
        } else { // Irregular verbs
            int n = (subject.equals ("je")) ?  1 : (subject.equals("tu")) ? 2 : (subject.equals("nous")) ? 4 : (subject.equals("vous")) ? 5 : (subject.equals("ils") || subject.equals("elles")) ? 6 : 3;
            conjugateVerb = readCsv[n];
        }
        return (Agreements.startsWithVowel(conjugateVerb) && (subject.equals("je") || subject.equals("te"))) ? subject.charAt(0)+"'"+conjugateVerb : subject + " "+conjugateVerb;
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
        String[] readCsv = ReadCSV.readRow("adjectives");
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
        String[] readCsv = ReadCSV.readRow(fileName);
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
        return simpleSentence();
    }
    public MainGenerator(String proficiency) {
        for (int i = 0; i < 10; i++) {
            System.out.println(simpleSentence());
        }
    }

    public static void main(String[] args) {
        new MainGenerator("A1");
    }
}
