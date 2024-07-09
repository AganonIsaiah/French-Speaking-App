package com.example.frenchlearningapp.service.generatorlogic;

/**
 * Constants class
 */
public final class Constants {


    public static final String[] INTERROGATIVES = {
            "qui",
            "que", "qu'est-ce que", "quoi",
            "quand",
            "où",
            "pourquoi",
            "quel", "quelle","quels","lequel","laquelle","lesquels",
            "comment", "combien"
    };

    // Possessive Adjectives
    public static final String[] POSSESSIVE_ADJ = {
            "mon","ma","mes",
            "ton","ta","tes",
            "son","sa","ses",
            "notre","nos",
            "votre","vos",
            "leur","leurs"
    };
    // Prepositions
    public static final String[] PREPOSITIONS = {
            "au","à la","aux","à l'", // à + definite article, au/aux no article
            "du","de la","des","de l'", // de + definite article, du/des no articles
            "dans", "sur", "sous", "devant", // 8 - 15
            "derrière","entre","par","vers"
    };


    public final static class Articles{
       // Indefinite Articles
       public static final String[] INDEFINITE_A = { "un","une","des" };
       // Partitive Articles
       public static final String[] PARTITIVE_A = {"du","de la","des","de l'"};
       // Definite Articles, M,F,V,P
       public static final String[] DEFINITE_A = {"le","la","les","l'"};
   }
    public final static class Pronouns {
        public static final String[] PRONOUNS = {
                "je",
                "tu",
                "il", "elle", "on",
                "nous",
                "vous",
                "ils", "elles"
        };
        // Indirect Pronouns
        public static final String[] INDIRECT_PN = {
                "me","m'",
                "te","t'",
                "lui",
                "nous",
                "vous",
                "leur"
        };

        // Direct Pronouns
        public static final String[] DIRECT_PN = {
                "me","m'",
                "te","t'",
                "le","la","l'",
                "nous",
                "vous",
                "les"
        };
        // Possessive Pronouns
        public static final String[] POSSESSIVE_PN = {
                "mien","mienne","miens","miennes", // mine
                "tien","tienne","tiens","tiennes", // yours
                "sien","sienne","siens","siennes", // his/hers/its
                "nôtre","nôtres",
                "vôtre","vôtres",
                "leur","leurs"
        };
        // Reflexive Pronouns
        public static final String[] REFLEXIVE_PN = {
                "je", "j'",
                "te", "t'",
                "se", "s'",// Il/Elle/On/Ils/Elles
                "nous",
                "vous",
        };
    }

    /**
     * Follows
     *      Adjective + Adverb
     *      Adverb + Verb
      */
    public final static  class Adverbs {
        public static final String[] TIME = {"jamais","parfois","souvent","toujours","bientôt"};
        public static final String[] PLACE = {"dehors","ici","là-bas","partout","quelque part"};
        public static final String[] QUANTITY = {"beaucoup","peu","très","trop", "autant","assez"}; // beacoup/peu/autant + de
        public static final String[] QUALITY = {"rapidement","lentement","mal","bien"};

    }

}

