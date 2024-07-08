package com.example.frenchlearningapp.service.generatorlogic;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * For generating nouns, adjectives and verbs from the resources folder
 */
public class ReadCSV {

    private static String file;
    private static List<String[]> fileInfo;
    private static int index = 0, getHeight = 0;


    /**
     * Returns specified file item
     *
     * @param fileName The file's name
     * @param row      Represents row access
     * @param col      Represents column access
     * @return item
     */
    public static String getFileItem(String fileName, int row, int col) {
        return Objects.requireNonNull(getFileInfo(fileName, row))[col];
    }

    /**
     * Preserves file row information
     *
     * @param fileName The file's name
     * @param row      Represents row access
     * @return The original file row
     */
    public static String[] getFileInfo(String fileName, int row) {
        String csvFilePath2 = "/frenchresources/" + fileName + ".csv";
        InputStream inputStream2 = ReadCSV.class.getResourceAsStream(csvFilePath2);
        // System.out.println("row: "+row);
        if (inputStream2 == null) {
            System.err.println("Resource not found: " + csvFilePath2);
            return null;
        }

        try (InputStreamReader inputStreamReader2 = new InputStreamReader(inputStream2);
             CSVReader reader2 = new CSVReader(inputStreamReader2)) {

            List<String[]> records = reader2.readAll();
            getHeight = records.size();
            return records.get(row);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Provides access to the original file array
     *
     * @return The original file array
     */
    public static int getIndex() {
        return index;
    }

    /**
     * Provides access to the original file name
     *
     * @return The original file name
     */
    public static String getName() {
        return file;
    }


    /**
     * Returns the irregular stem for each tense
     *
     * @param v The verb
     * @param tense The current tense
     * @return The irregular stem of the verb to be conjugated, flag if the verb is not irregular
     */
    public static String getIrregularStem(String v, String tense) {
        int n = -1;

        String file1 = "irregular_verbs";
        switch (tense) {
            // 7 -> Future Stem
            case "future" -> n = 8;
            case "imparfait" -> n = 9;
            case "past participle", "passe compose" -> n = 10;
            case "vandertramp" -> {
                n = 1; // 2nd col, past participle
                file1 = "vandertramp_verbs";
            }
            case "passe simple" -> n = 11;
        }

        int check = isFileType(v,file1)[1];
        if (check == -1) return "flag";

        return getFileItem(file1, check, n);
    }

    /**
     * Traverses through irregular_verbs file to determine if verb is irregular
     *
     * @param verb The verb to find in the file
     * @param fileName vandertramp_verbs.csv or irregular_verbs.csv
     * @return Index 0 - 1 for true, 0 for false, Index 1 - The row
     */
    public static int[] isFileType(String verb, String fileName) {
        getFileInfo(fileName, 1);

        for (int rowIndex = 0; rowIndex < getHeight; rowIndex++) {
            for (int j = 0; j < 1; j++) {
                String n = Objects.requireNonNull(getFileInfo(fileName, rowIndex))[j];
                if (verb.equals(n)) return new int[]{1, rowIndex};
            }
        }
        return new int[]{0, -1};
    }



    /**
     * For getting nouns, adjectives, verbs from resources folder
     *
     * @param fileName Directed file from frenchresources tab
     * @return Array containing gendered/plural form of word
     */
    public static String[] readFile(String fileName) {
        String csvFilePath = "/frenchresources/" + fileName + ".csv";
        file = fileName;
        InputStream inputStream = ReadCSV.class.getResourceAsStream(csvFilePath);
        if (inputStream == null) {
            System.err.println("Resource not found: " + csvFilePath);
            return null;
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             CSVReader reader = new CSVReader(inputStreamReader)) {

            List<String[]> records = reader.readAll();
            fileInfo = records;

            // Exclude header row if present
            int maxRowIndex = records.size() - 1;
            if (maxRowIndex <= 0) {
                System.out.println("Empty CSV file: " + fileName);
                return null;
            }

            // Generate a random index within the valid range
            Random rand = new Random();
            index = rand.nextInt(maxRowIndex) + 1; // Random index from 1 to maxRowIndex

            // Return the randomly selected row
            return records.get(index);

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return null;
        }
    }
}
