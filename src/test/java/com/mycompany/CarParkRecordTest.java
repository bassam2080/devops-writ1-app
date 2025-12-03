package com.mycompany;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CarParkRecord class functions accessible from the Record Menu:
 * - parseRecord() - Main record processing function
 * - printRecordDebug() - Debug output function
 * - OverwriteLine() - Line overwriting function (private, tested indirectly)
 */
public class CarParkRecordTest {

    private static final String TEST_FILE_PATH = "src/test/resources/test_record.csv";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() throws IOException {
        // Create test directory if not exists
        Files.createDirectories(Paths.get("src/test/resources"));

        // Create empty test CSV file
        Files.writeString(Paths.get(TEST_FILE_PATH), "");

        // Capture System.out
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Restore original streams
        System.setOut(originalOut);

        // Clean up test file
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    }

    /**
     * Test for Record Menu: parseRecord() - Register new vehicle
     */
    @Test
    @DisplayName("Record Mode: parseRecord should register new vehicle")
    void testParseRecord_RegistersNewVehicle() throws IOException {
        CarParkRecord record = new CarParkRecord(TEST_FILE_PATH);

        record.parseRecord("TEST123");

        String output = outContent.toString();
        assertTrue(output.contains("Registering vehicle"), "Should indicate registering new vehicle");

        // Verify the VRN was written to file
        String fileContent = Files.readString(Paths.get(TEST_FILE_PATH));
        assertTrue(fileContent.contains("TEST123"), "File should contain new VRN");
    }

    @Test
    @DisplayName("Record Mode: parseRecord should detect existing vehicle entry")
    void testParseRecord_DetectsExistingVehicle() throws IOException {
        // Pre-populate file with existing entry (VRN with entry date/time but no exit)
        String existingEntry = "EXIST123,2024-01-15,10:00:00\n";
        Files.writeString(Paths.get(TEST_FILE_PATH), existingEntry);

        CarParkRecord record = new CarParkRecord(TEST_FILE_PATH);

        record.parseRecord("EXIST123");

        String output = outContent.toString();
        assertTrue(output.contains("Vehicle Found"), "Should indicate vehicle was found");
    }

    @Test
    @DisplayName("Record Mode: parseRecord with case-insensitive VRN matching")
    void testParseRecord_CaseInsensitiveMatch() throws IOException {
        // Pre-populate with uppercase VRN
        String existingEntry = "UPPER123,2024-01-15,10:00:00\n";
        Files.writeString(Paths.get(TEST_FILE_PATH), existingEntry);

        CarParkRecord record = new CarParkRecord(TEST_FILE_PATH);

        // Search with lowercase
        record.parseRecord("upper123");

        String output = outContent.toString();
        assertTrue(output.contains("Vehicle Found"), "Should find VRN regardless of case");
    }

    @Test
    @DisplayName("Record Mode: parseRecord registers vehicle exit")
    void testParseRecord_RegistersVehicleExit() throws IOException {
        // Pre-populate with entry record (VRN, entry date, entry time)
        String existingEntry = "EXIT123,2024-01-15,08:00:00\n";
        Files.writeString(Paths.get(TEST_FILE_PATH), existingEntry);

        CarParkRecord record = new CarParkRecord(TEST_FILE_PATH);

        record.parseRecord("EXIT123");

        String output = outContent.toString();
        assertTrue(output.contains("Vehicle Found"), "Should detect existing vehicle");

        // File should contain exit time entry
        String fileContent = Files.readString(Paths.get(TEST_FILE_PATH));
        assertTrue(fileContent.contains("EXIT123"), "File should still contain VRN");
    }

    @Test
    @DisplayName("Record Mode: parseRecord handles empty VRN")
    void testParseRecord_EmptyVRN() {
        CarParkRecord record = new CarParkRecord(TEST_FILE_PATH);

        record.parseRecord("");

        String output = outContent.toString();
        assertTrue(output.contains("Registering vehicle"), "Should attempt to register empty VRN");
    }

    @Test
    @DisplayName("Record Mode: parseRecord handles VRN with special characters")
    void testParseRecord_SpecialCharactersInVRN() throws IOException {
        CarParkRecord record = new CarParkRecord(TEST_FILE_PATH);

        record.parseRecord("AB12 XYZ");

        String fileContent = Files.readString(Paths.get(TEST_FILE_PATH));
        assertTrue(fileContent.contains("AB12 XYZ"), "Should handle VRN with space");
    }

    /**
     * Test for printRecordDebug() function
     */
    @Test
    @DisplayName("Record Mode: printRecordDebug outputs vehicle record elements")
    void testPrintRecordDebug_OutputsElements() {
        String[] testRecord = {"VRN123", "2024-01-15", "10:00:00"};

        CarParkRecord.printRecordDebug(testRecord, 3);

        String output = outContent.toString();
        assertTrue(output.contains("vehicleRecord 0 : VRN123"), "Should print first element");
        assertTrue(output.contains("vehicleRecord 1 : 2024-01-15"), "Should print second element");
        assertTrue(output.contains("vehicleRecord 2 : 10:00:00"), "Should print third element");
    }

    @Test
    @DisplayName("Record Mode: printRecordDebug with partial elements")
    void testPrintRecordDebug_PartialElements() {
        String[] testRecord = {"VRN123", "2024-01-15", "10:00:00", "2024-01-15", "18:00:00"};

        // Only print first 2 elements
        CarParkRecord.printRecordDebug(testRecord, 2);

        String output = outContent.toString();
        assertTrue(output.contains("vehicleRecord 0 : VRN123"), "Should print first element");
        assertTrue(output.contains("vehicleRecord 1 : 2024-01-15"), "Should print second element");
        assertFalse(output.contains("vehicleRecord 2"), "Should not print third element");
    }

    @Test
    @DisplayName("Record Mode: printRecordDebug with empty array")
    void testPrintRecordDebug_EmptyArray() {
        String[] testRecord = {};

        CarParkRecord.printRecordDebug(testRecord, 0);

        // Should complete without error
        String output = outContent.toString();
        assertFalse(output.contains("vehicleRecord"), "Should not print any elements");
    }

    /**
     * Test for multiple vehicle registrations
     */
    @Test
    @DisplayName("Record Mode: Multiple vehicles can be registered")
    void testParseRecord_MultipleVehicles() throws IOException {
        CarParkRecord record = new CarParkRecord(TEST_FILE_PATH);

        record.parseRecord("CAR001");
        record.parseRecord("CAR002");
        record.parseRecord("CAR003");

        String fileContent = Files.readString(Paths.get(TEST_FILE_PATH));
        assertTrue(fileContent.contains("CAR001"), "Should contain first vehicle");
        assertTrue(fileContent.contains("CAR002"), "Should contain second vehicle");
        assertTrue(fileContent.contains("CAR003"), "Should contain third vehicle");
    }

    @Test
    @DisplayName("Record Mode: Constructor initializes with file path")
    void testCarParkRecord_Constructor() {
        CarParkRecord record = new CarParkRecord(TEST_FILE_PATH);
        assertNotNull(record, "Should create CarParkRecord instance");
    }
}

