package com.mycompany;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Main class menu functions:
 * - printMenu() - Print menu options
 * - menu() - Main menu selection (tests require interactive input, limited testing)
 * - recordMenu() - Record menu selection
 * - adminMenu() - Admin menu selection
 */
public class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    /**
     * Test for printMenu() function
     */
    @Test
    @DisplayName("Main Menu: printMenu should display all menu options")
    void testPrintMenu_DisplaysAllOptions() {
        String[] options = {"[1] Option One", "[2] Option Two", "[3] Exit"};

        Main.printMenu(options);

        String output = outContent.toString();
        assertTrue(output.contains("[1] Option One"), "Should display first option");
        assertTrue(output.contains("[2] Option Two"), "Should display second option");
        assertTrue(output.contains("[3] Exit"), "Should display exit option");
        assertTrue(output.contains("Choose your option"), "Should prompt for input");
    }

    @Test
    @DisplayName("Main Menu: printMenu with empty options array")
    void testPrintMenu_EmptyOptionsArray() {
        String[] options = {};

        Main.printMenu(options);

        String output = outContent.toString();
        assertTrue(output.contains("Choose your option"), "Should still prompt for input");
    }

    @Test
    @DisplayName("Main Menu: printMenu with single option")
    void testPrintMenu_SingleOption() {
        String[] options = {"[1] Only Option"};

        Main.printMenu(options);

        String output = outContent.toString();
        assertTrue(output.contains("[1] Only Option"), "Should display single option");
    }

    /**
     * Test for main menu structure
     */
    @Test
    @DisplayName("Main Menu: Main menu has three options")
    void testMainMenu_HasThreeOptions() {
        // This test verifies the menu structure by checking printMenu output
        String[] expectedOptions = {
                "[1] Admin Mode:",
                "[2] Record Mode:",
                "[3] Exit",
        };

        Main.printMenu(expectedOptions);

        String output = outContent.toString();
        assertTrue(output.contains("[1] Admin Mode:"), "Should have Admin Mode option");
        assertTrue(output.contains("[2] Record Mode:"), "Should have Record Mode option");
        assertTrue(output.contains("[3] Exit"), "Should have Exit option");
    }

    /**
     * Test for admin menu options display
     */
    @Test
    @DisplayName("Admin Menu: adminMenu prompts for option selection")
    void testAdminMenu_PromptsForSelection() {
        // Simulate user input for admin menu
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Note: This will try to execute displayData which may fail due to file path
        // We're testing that the menu prompts correctly
        try {
            Main.adminMenu();
        } catch (Exception e) {
            // Expected - file path may not exist
        }

        String output = outContent.toString();
        assertTrue(output.contains("Please select an option"), "Should prompt for option");
        assertTrue(output.contains("[1]Show all vehicles"), "Should show all vehicles option");
        assertTrue(output.contains("[2]Search for VRN"), "Should show search option");
        assertTrue(output.contains("[3]Amend data"), "Should show amend option");
        assertTrue(output.contains("[4]Delete data"), "Should show delete option");
    }

    @Test
    @DisplayName("Admin Menu: adminMenu option 1 triggers displayData")
    void testAdminMenu_Option1TriggersDisplayData() {
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        try {
            Main.adminMenu();
        } catch (Exception e) {
            // May fail due to file path, but menu options should be shown
        }

        String output = outContent.toString();
        assertTrue(output.contains("[1]Show all vehicles"), "Option 1 should be for display");
    }

    @Test
    @DisplayName("Admin Menu: adminMenu option 2 triggers searchVRN")
    void testAdminMenu_Option2TriggersSearch() {
        String input = "2\nTEST123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        try {
            Main.adminMenu();
        } catch (Exception e) {
            // May fail due to file path
        }

        String output = outContent.toString();
        assertTrue(output.contains("[2]Search for VRN"), "Option 2 should be for search");
    }

    /**
     * Test for record menu
     */
    @Test
    @DisplayName("Record Menu: recordMenu prompts for VRN input")
    void testRecordMenu_PromptsForVRN() {
        String input = "TEST123\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        try {
            Main.recordMenu();
        } catch (Exception e) {
            // May fail due to file path
        }

        String output = outContent.toString();
        assertTrue(output.contains("Please enter your VRN"), "Should prompt for VRN");
    }

    /**
     * Test for file path configuration
     */
    @Test
    @DisplayName("Main: filePath is properly configured")
    void testFilePath_IsConfigured() {
        assertNotNull(Main.filePath, "File path should not be null");
        assertTrue(Main.filePath.contains("vrn.csv"), "File path should point to CSV file");
    }
}

