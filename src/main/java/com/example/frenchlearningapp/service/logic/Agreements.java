package com.example.frenchlearningapp.service.logic;

/**
 * Accounts for gender/quantity agreements in nouns_inanimate.csv and vandertramp_csv
 *
 *
 */
public class Agreements {



    /**
     * Follows agreement pattern for Dr Mrs Vandertramp verbs
     *
     * @param v The verb (In past participle form)
     * @param type SM,SF,PM,PF
     * @return Correct form of past participle verb
     */
    public String vanderTramp(String v,String type){
        return (type.equals("SF")) ? v+"e" : (type.equals("PM")) ? v+"s": (type.equals("PF")) ? v+"es":v;
    }

    /**
     * Follows agreement pattern for inanimate nouns
     *
     * @param n The inanimate noun
     * @param type S for singular, P for plural
     * @return Plural form of noun
     */
    public String inNouns(String n, char type){
        return (type == 'P') ? n +"s" : n;
    }
}
