package com.example.frenchlearningapp.service.logic;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

/**
 * For generating nouns, adjectives and verbs from the resources folder
 */
public class ReadCSV {

    /**
     * For getting nouns, adjectives, verbs from resources folder
     *
     * @param fileName Directed file from frenchresources tab
     * @return Array containing gendered/plural form of word
     */
    public static String[] readRow(String fileName) {
        String csvFilePath = "/frenchresources/"+fileName+".csv";

        InputStream inputStream = ReadCSV.class.getResourceAsStream(csvFilePath);
        if (inputStream == null) {
            System.err.println("Resource not found: " + csvFilePath);
            return null;
        }

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             CSVReader reader = new CSVReader(inputStreamReader)) {

            List<String[]> records = reader.readAll();

            // Exclude header row if present
            int maxRowIndex = records.size() - 1;
            if (maxRowIndex <= 0) {
                System.out.println("Empty CSV file: " + fileName);
                return null;
            }

            // Generate a random index within the valid range
            Random rand = new Random();
            int randomIndex = rand.nextInt(maxRowIndex) + 1; // Random index from 1 to maxRowIndex

            // Return the randomly selected row
            return records.get(randomIndex);

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return null;
        }
    }
}
