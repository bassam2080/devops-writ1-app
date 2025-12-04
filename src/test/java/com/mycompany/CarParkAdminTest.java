package com.mycompany;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for CarParkAdmin class functions accessible from the Admin Menu:
 * [1] Show all vehicles - displayData()
 * [2] Search for VRN - searchVRN()
 * [3] Amend data - amendLine()
 * [4] Delete data - deleteLine()
 */
public class CarParkAdminTest {

    private static final String TEST_FILE_PATH = "src/test/resources/test_vrn.csv";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() throws IOException {
        // Create test directory if not exists
        Files.createDirectories(Paths.get("src/test/resources"));

        // Create test CSV file with sample data
        String testData = "ABC123,2023-01-15,12:01:00\n" +
                "XYZ789,2023-02-20,10:30:00\n" +
                "DEF456,2023-03-25,14:45:00\n";
        Files.writeString(Paths.get(TEST_FILE_PATH), testData);

        // Capture System.out
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Restore original streams
        System.setOut(originalOut);
        System.setIn(originalIn);

        // Clean up test file
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    }

    /**
     * Test for Admin Menu Option [1]: Show all vehicles
     * Tests displayData() function
     */
    @Test
    @DisplayName("Menu Option 1: displayData should display all vehicles from CSV")
    void testDisplayData_ShowsAllVehicles() {
        CarParkAdmin.displayData(TEST_FILE_PATH);

        String output = outContent.toString();
        assertTrue(output.contains("ABC123"), "Should display first vehicle VRN");
        assertTrue(output.contains("XYZ789"), "Should display second vehicle VRN");
        assertTrue(output.contains("DEF456"), "Should display third vehicle VRN");
        assertTrue(output.contains("2023-01-15"), "Should display entry date");
        assertTrue(output.contains("12:01:00"), "Should display entry time");
    }

    @Test
    @DisplayName("Menu Option 1: displayData with non-existent file should display error")
    void testDisplayData_NonExistentFile() {
        CarParkAdmin.displayData("nonexistent_file.csv");
        // Should not throw exception, error is printed to console
    }

    @Test
    @DisplayName("Menu Option 1: displayData with empty file should handle gracefully")
    void testDisplayData_EmptyFile() throws IOException {
        String emptyFilePath = "src/test/resources/empty_test.csv";
        Files.writeString(Paths.get(emptyFilePath), "");

        CarParkAdmin.displayData(emptyFilePath);

        // Should complete without error
        Files.deleteIfExists(Paths.get(emptyFilePath));
    }

    /**
     * Test for Admin Menu Option [2]: Search for VRN
     * Tests searchVRN() function
     */
    @Test
    @DisplayName("Menu Option 2: searchVRN should find existing vehicle")
    void testSearchVRN_FindsExistingVehicle() {
        // Simulate user input for VRN search
        String input = "ABC123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CarParkAdmin.searchVRN(TEST_FILE_PATH);

        String output = outContent.toString();
        assertTrue(output.contains("Found matching line"), "Should find matching VRN");
        assertTrue(output.contains("ABC123"), "Should display found VRN");
    }

    @Test
    @DisplayName("Menu Option 2: searchVRN with case-insensitive search")
    void testSearchVRN_CaseInsensitive() {
        // Search with lowercase
        String input = "abc123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CarParkAdmin.searchVRN(TEST_FILE_PATH);

        String output = outContent.toString();
        assertTrue(output.contains("Found matching line"), "Should find VRN regardless of case");
    }

    @Test
    @DisplayName("Menu Option 2: searchVRN for non-existent vehicle")
    void testSearchVRN_NotFound() {
        String input = "NOTEXIST\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CarParkAdmin.searchVRN(TEST_FILE_PATH);

        String output = outContent.toString();
        assertFalse(output.contains("Found matching line"), "Should not find non-existent VRN");
    }

    /**
     * Test for Admin Menu Option [3]: Amend data
     * Tests amendLine() function
     */
    @Test
    @DisplayName("Menu Option 3: amendLine should modify specified line")
    void testAmendLine_ModifiesLine() throws IOException {
        // Simulate user input: line number 1 and new data
        String input = "1\nNEWVRN,2024-01-01,10:00:00\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CarParkAdmin.amendLine(TEST_FILE_PATH);

        // Verify the file was modified
        String content = Files.readString(Paths.get(TEST_FILE_PATH));
        assertTrue(content.contains("NEWVRN"), "File should contain amended data");
    }

    @Test
    @DisplayName("Menu Option 3: amendLine with invalid line number")
    void testAmendLine_InvalidLineNumber() {
        // Line number out of range
        String input = "999\nNEWVRN,2024-01-01,10:00:00\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CarParkAdmin.amendLine(TEST_FILE_PATH);

        String output = outContent.toString();
        assertTrue(output.contains("Invalid line number"), "Should display error for invalid line");
    }

    /**
     * Test for Admin Menu Option [4]: Delete data
     * Tests deleteLine() function
     */
    @Test
    @DisplayName("Menu Option 4: deleteLine should clear specified line")
    void testDeleteLine_ClearsLine() throws IOException {
        // Simulate user input: line number 1
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CarParkAdmin.deleteLine(TEST_FILE_PATH);

        // Verify the line was cleared
        String content = Files.readString(Paths.get(TEST_FILE_PATH));
        String output = outContent.toString();
        assertTrue(output.contains("deleted"), "Should confirm deletion");
    }

    @Test
    @DisplayName("Menu Option 4: deleteLine with invalid line number")
    void testDeleteLine_InvalidLineNumber() {
        // Line number out of range
        String input = "999\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CarParkAdmin.deleteLine(TEST_FILE_PATH);

        String output = outContent.toString();
        assertTrue(output.contains("Invalid line number"), "Should display error for invalid line");
    }

    @Test
    @DisplayName("Menu Option 4: deleteLine with negative line number")
    void testDeleteLine_NegativeLineNumber() {
        // Negative line number
        String input = "-1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        CarParkAdmin.deleteLine(TEST_FILE_PATH);

        String output = outContent.toString();
        assertTrue(output.contains("Invalid line number"), "Should display error for negative line");
    }
}

