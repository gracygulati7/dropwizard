package com.flipfit.helper;

import java.util.Scanner;

public class InputValidator {
    
    /**
     * Safely read an integer from Scanner with error handling
     */
    public static int readInt(Scanner sc) {
        while (true) {
            try {
                return sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                sc.nextLine(); // Clear invalid input
                System.out.print("Invalid input! Please enter a valid number: ");
            }
        }
    }
    
    /**
     * Safely read an integer with a prompt
     */
    public static int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        return readInt(sc);
    }
    
    /**
     * Read a choice between min and max with validation
     */
    public static int readChoice(Scanner sc, int min, int max) {
        while (true) {
            int choice = readInt(sc);
            if (choice >= min && choice <= max) {
                return choice;
            }
            System.out.print("Invalid choice! Please enter a number between " + min + " and " + max + ": ");
        }
    }
}
