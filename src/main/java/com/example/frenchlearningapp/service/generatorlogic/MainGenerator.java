package com.example.frenchlearningapp.service.generatorlogic;

import java.util.Objects;
import java.util.Random;

import com.example.frenchlearningapp.service.generatorlogic.conjugations.FutureConjugations;
import com.example.frenchlearningapp.service.generatorlogic.conjugations.PastConjugations;
import com.example.frenchlearningapp.service.generatorlogic.conjugations.PresentConjugations;

/**
 * NOTE - Conjugations are good, connections need work
 */
public class MainGenerator {

    private static Random rand = new Random();
    private static String proficiency;

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

        // Random case
        if (tense.equals("random")) {
            n = rand.nextInt(3);
            tense = (n == 0) ? "present" : (n == 1) ? "past" : "future";
        }

        /* Get Subject + Verb (Tense) */
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
     * Most Follow This Pattern: Article + Noun + Adjective
     * B.A.N.G.S (Beauty/Age/Number/Goodness/Size Adjectives): Article + Adjective + Noun
     *
     * @param s The sentence with the Article + Noun
     * @param gender Adjective must agree with Noun
     * @return Sentence with Article, Noun, Adjectives
     */
    private static String getAdjectives( String s, String gender){
        String[] readRow = ReadCSV.readFile("adjectives");
        assert readRow != null;
        StringBuilder sentence = new StringBuilder(s);
        sentence.append(" ");

        // Before a noun: Article + Adjective + Noun
        if (readRow[1].equals("B")){
            // If elision
            int flag = Agreements.checkElision(s);

            if (flag != -1) {
                String firstHalf =sentence.substring(0,flag), secondHalf = sentence.substring(flag+1,sentence.length());
                sentence.setLength(0);
                sentence.append(readRow[0]).append(" ").append(secondHalf);

                switch(gender){
                    case "SM" -> {




                    }
                    case "SF" -> sentence.append(readRow[3]);
                }
            }


            // Normal case
        } else { // After a noun: Article + Noun + Adjective
            switch(gender){
                case "SM" -> sentence.append(readRow[2]);
                case "SF" -> sentence.append(readRow[3]);
                case "PM" -> sentence.append(readRow[4]);
                case "PF" -> sentence.append(readRow[5]);
            }
        }
        return sentence.toString();
    }


    /**
     * Places an article before a noun/adjective
     *
     * @param s The string to concatenate the article with
     * @param nounType L - lieu, O - Objet, P - Personne
     * @param genderType SM/SF/PM/PF
     * @return The article and the noun
     */
    private static String[] getArticles(String s, char nounType, String genderType){
        StringBuilder sentence = new StringBuilder();

        if (nounType == 'L'){ // Preposition + Definite Article + Lieu
            int n = rand.nextInt(5);
            switch (n) {
                case 0 -> { // à, de
                    int i = (rand.nextInt(2) == 0) ? 0: 4;
                    if (Agreements.startsWithVowel(s) && genderType.charAt(0) != 'P') sentence.append(Constants.PREPOSITIONS[3+i]);
                    else if (genderType.charAt(0) == 'P') sentence.append(Constants.PREPOSITIONS[2+i]);
                    else if (genderType.charAt(1) == 'M') sentence.append(Constants.PREPOSITIONS[i]);
                    else if (genderType.charAt(1) == 'F') sentence.append(Constants.PREPOSITIONS[1+i]);
                }
                case 1, 2,3,4 -> { // dans, sur, sous, par, vers, devant, derrière
                    sentence.append(Constants.PREPOSITIONS[rand.nextInt(8) + 8]).append(" ");
                    if (Agreements.startsWithVowel(s) && genderType.charAt(0) != 'P') sentence.append(Constants.Articles.DEFINITE_A[3]);
                    else if (genderType.charAt(0) == 'P') sentence.append(Constants.Articles.DEFINITE_A[2]);
                    else if (genderType.charAt(1) == 'M') sentence.append(Constants.Articles.DEFINITE_A[0]);
                    else if (genderType.charAt(1) == 'F') sentence.append(Constants.Articles.DEFINITE_A[1]);
                }
            }
        }
        else { // Object/Person case
            // Partitive Article/Definite Article/Indefinite Article + noun (Objet)
            // Definite Article/Indefinite Article + noun (Personne)
            int i = rand.nextInt(nounType == 'P' ? 2 : 3);
            String [] arr = (i == 0) ? Constants.Articles.DEFINITE_A: (i == 1) ? Constants.Articles.INDEFINITE_A: Constants.Articles.PARTITIVE_A;
            if (Agreements.startsWithVowel(s) && genderType.charAt(0) != 'P' && i != 1) sentence.append(arr[3]);
            else if (genderType.charAt(0) == 'P') sentence.append(arr[2]);
            else if (genderType.charAt(1) == 'M') sentence.append(arr[0]);
            else if (genderType.charAt(1) == 'F') sentence.append(arr[1]);
        }

        String articleNoun = Agreements.elision(sentence.toString(),s,sentence.charAt(sentence.length()-1) != 's');
        String noun = s;
        String gender = genderType;
        String article = sentence.toString();
       return new String[]{articleNoun,noun,gender,article};
    }

    /**
     * Generates an article with respect to the generated noun
     *
     * @param amp Gender/Quantity of Noun
     * @return Index 0 - Noun
     *         Index 1 - Type L - Lieu, P - Personne, O - Objet
     *         Index 2 - Gender SM/SF/PM/PF
     */
    private static String[] getNouns(String amp) {
        rand = new Random();

        /* Generate a noun from the animate/inanimate files */
        int typeInt = rand.nextInt(2); // 0 for inanimate, 1 for animate
        String fileName = (typeInt == 0) ? "nouns_inanimate" : "nouns_animate";
        String[] nounRow = ReadCSV.readFile(fileName); // animate - 0 singular, 1 plural. Inanimate - 0 noun, 1 - 5 sing/plural
        String noun = "", gender="",type;
        assert nounRow != null;

        /* Get noun */
        if (typeInt == 0){ // Inanimate case
            int n =rand.nextInt(2);
            noun = nounRow[n];
            type = nounRow[2];
            gender = (n == 0) ? "S" + nounRow[3]: "P" +nounRow[3];
        } else { // Animate case
            int n = 2; //rand.nextInt(4) + 2; // starts at ind 2
             switch(amp){
                 case "elle" -> n = 3;
                 case "ils" -> n = 4;
                 case "elles" -> n = 5;
             }
            noun = nounRow[n];
            type = nounRow[1];
            gender = (n == 2) ? "SM" : (n == 3) ? "SF" : (n == 4) ? "PM" : "PF";
        }

        return new String[]{noun,type,gender};
    }

    public static String getSentence(String proficiency){
        String subject = getSubject();
        String[] s = getNouns(subject);
        String article = getArticles(s[0],s[1].charAt(0),s[2])[0];
        return getVerb(subject, "random") + " "+ getAdjectives(article,s[2]);
    }
    public MainGenerator(String proficiency) {
        this.proficiency = proficiency;
        for (int i = 0; i < 10; i++) {
            System.out.println(getSentence("a1"));
        }
    }

    public static void main(String[] args) {
        new MainGenerator("A1");
    }
}
