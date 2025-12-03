/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany;

import java.util.Scanner;

import static java.lang.System.exit;

/**
 *
 * @author st20287200
 */
public class Main {
    public static String filePath = "newvrn.csv"; //File path to csv file
    public static Scanner scanner; // Create a class-level scanner

    public static void main(String[] args) {
        scanner = new Scanner(System.in); // Initialize once
        menu();
        scanner.close(); // Close when done
    }

    public static void printMenu(String[] options){
        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }

    public static void menu() {
        String[] options = {
                "[1] Admin Mode:",
                "[2] Record Mode:",
                "[3] Exit",
        };
        int option = 1;
        while (option != 3){
            printMenu(options);
            try {
                option = scanner.nextInt();
                switch (option){
                    case 1: adminMenu(); break;
                    case 2: recordMenu(); break;
                    case 3: exit(0);
                }
            }
            catch (Exception ex){
                System.out.println("Please enter an integer value between 1 and " + options.length);
                scanner.nextLine(); // Clear the entire line including newline
            }
        }
    }

    public static void recordMenu() {
        System.out.println("Please enter your VRN: ");
        String selection = scanner.nextLine(); // Use the class-level scanner
        com.mycompany.CarParkRecord myCarParkRecord = new com.mycompany.CarParkRecord(filePath);
        myCarParkRecord.parseRecord(selection);
    }

    public static void adminMenu() {
        System.out.println("Please select an option: ");
        System.out.println("[1]Show all vehicles: ");
        System.out.println("[2]Search for VRN: ");
        System.out.println("[3]Amend data: ");
        System.out.println("[4]Delete data");

        scanner.nextLine(); // Clear any leftover input from previous nextInt()
        String selection = scanner.nextLine(); // Use the class-level scanner

        switch(selection) {
            case "1":
                com.mycompany.CarParkAdmin.displayData(filePath);
                break;
            case "2":
                com.mycompany.CarParkAdmin.searchVRN(filePath);
                break;
            case "3":
                com.mycompany.CarParkAdmin.amendLine(filePath);
                break;
            case "4":
                com.mycompany.CarParkAdmin.deleteLine(filePath);
                break;
        }
    }
}