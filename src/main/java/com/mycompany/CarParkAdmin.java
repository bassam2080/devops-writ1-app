package com.mycompany;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;



public class CarParkAdmin {

    public static void displayData(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Error: File '" + filePath + "' does not exist.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { //Create a new reader object
            String line;
            while ((line = br.readLine()) != null) { //While each line is not empty
                String[] values = line.split(","); //Create an array that contains all the csv values

                for (String value : values) {

                    System.out.print(value + " | ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void searchVRN(String filePath) {
        Scanner userInput = new Scanner(System.in); //Create new scanner object
        System.out.println("Please enter your VRN: ");
        String vrn = userInput.nextLine(); //Store input in a variable called vrn

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath)); //Create new reader object
            String line;

            while ((line = reader.readLine()) != null) {
                String[] elements = line.split(","); //Create an array that stores all the csv data

                if (elements.length > 0 && elements[0].trim().replace("ï»¿", "").equalsIgnoreCase(vrn)) { //If a vrn is located within the csv file then display this message
                    System.out.println("Found matching line: " + line.replace("ï»¿", ""));
                }
            }

            reader.close(); //Close the reader object
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void amendLine(String filePath) {
        try {
            Scanner userInput = new Scanner(System.in);
            System.out.println("Enter the line number to amend (starting from 0): ");
            int lineNumber = userInput.nextInt();
            userInput.nextLine(); // Consume the newline character left by nextInt()

            System.out.println("Enter the new data for the line: ");
            String newData = userInput.nextLine(); //sets new data to user input

            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path); //reads all lines in csv

            if (lineNumber >= 0 && lineNumber < lines.size()) {
                lines.set(lineNumber - 1, newData); //sets line choice to user input

                Files.write(path, lines);
                System.out.println("Line amended successfully."); //confirmation
            } else {
                System.out.println("Invalid line number. Please provide a valid line number."); //if line is invalid
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void deleteLine(String filePath) {
        try {
            Scanner userInput = new Scanner(System.in);
            System.out.println("Enter the line number to delete (starting from 0): ");
            int lineNumber = userInput.nextInt();
            userInput.nextLine(); // Consume the newline character left by nextInt()

            System.out.println("Enter the new data for the line: ");
            String newData = ""; //sets new data to user input

            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path); //reads all lines in csv

            if (lineNumber >= 0 && lineNumber < lines.size()) {
                lines.set(lineNumber - 1, newData); //sets line choice to user input

                Files.write(path, lines);
                System.out.println("Line deleted  successfully."); //confirmation
            } else {
                System.out.println("Invalid line number. Please provide a valid line number."); //if line is invalid
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
