package com.mycompany;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class CarParkRecord {

    private String filePath;

    public CarParkRecord(String filePath) {
        this.filePath = filePath;
    }
    public static void printRecordDebug(String[] vehicleRecord, int numElements){
        for (int i=0; i<numElements; i++ )
        {
            System.out.println("vehicleRecord " + i + " : " + vehicleRecord[i]);
        }
    }

    public void parseRecord(String VRN)  {
        Date date = Calendar.getInstance().getTime(); //Get the current date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date); //Format the date into a string

        String toDelete = "";
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss"); //Get the current time HH:MM:SS
        String strHour = hourFormat.format(date); //Format the time into a string

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath)); //Create a new reader object
            BufferedWriter myWriter = new BufferedWriter(new FileWriter(filePath, true)); //Create a new writer object
            String line;

            boolean vehicleFound = false; //A boolean variable that is changed to true of the VRN has been located
            while ((line = reader.readLine()) != null) { //While each line of the csv is not empty run a loop
                String[] vehicleRecord = line.split(","); //Create an array called vehicleRecord that holds all the csv data
                for (String s : vehicleRecord) {
                    // Vehicle exist
                    if (s.trim().replace("ï»¿", "").equalsIgnoreCase(VRN)) {
                        System.out.println("Vehicle Found: " + line.replace("ï»¿", "")); //If the vrn already exists, display this message

                        if (vehicleRecord.length == 1) {
                            System.out.println("Bad Vehicle Record " + vehicleRecord[0]);
                            break;
                        }

                        printRecordDebug(vehicleRecord,3);

                        if (vehicleRecord.length <= 3) {
                            // vehicle found with entry time but no exit time, therefore vehicle leaving
                            vehicleFound = true;
                            toDelete = line;
                            try {
                                myWriter.write(VRN + "," + vehicleRecord[1] + "," + vehicleRecord[2] + "," + strDate + "," + strHour); //Write the new VRN and date time to the csv
                                myWriter.newLine();
                            } catch (IOException e) {
                                System.out.println("Error occurred");
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }

            if (!vehicleFound)  {
                System.out.println("Registering vehicle"); //If the boolean value is false then run this code
                try {
                    myWriter.write(VRN + "," + strDate + "," + strHour); //Write the new VRN and date time to the csv
                    myWriter.newLine();
                    //vehicleFound = false;
                } catch (IOException e) {
                    System.out.println("Error occurred");
                    e.printStackTrace();
                }
            }

            // Close the writer outside the loop
            try {
                myWriter.close();
            } catch (IOException e) {
                System.out.println("Error closing writer");
                e.printStackTrace();
            }

            reader.close();
            myWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (!toDelete.isEmpty())
        {
            OverwriteLine(toDelete);
        }
    }

    private void  OverwriteLine(String lineToErase) {
        //Instantiating the Scanner class to read the file
        Scanner sc;
        try {
            sc = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //instantiating the StringBuffer class
        StringBuilder buffer = new StringBuilder();
        //Reading lines of the file and appending them to StringBuffer
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()).append(System.lineSeparator());
        }
        String fileContents = buffer.toString();
        System.out.println("Contents of the file: " + fileContents);
        //closing the Scanner object
        sc.close();
        String newLine = "";
        //Replacing the old line with new line
        fileContents = fileContents.replaceFirst(lineToErase, newLine);
        //instantiating the FileWriter class
        FileWriter writer;
        try {
            writer = new FileWriter(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.append(fileContents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
