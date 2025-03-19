package validation;

import java.util.Scanner;

public class validation {
    // Make sure that maximumCapacity is less than totalTickets.
    public static boolean isMaximumCapacityValid(int maximumCapacity, int totalTickets) {
        return maximumCapacity < totalTickets; // Return true if maximumCapacity is valid.
    }

    // Ask the user for integer input, making sure that the input is valid.
    public static int AskForInput(Scanner scanner, String AskMessage) {
        int integerValue; // Variable to store the validated integer input.
        while (true) { // Repeat until the valid input is provided.
            System.out.println(AskMessage); // Display the message asking user for input.
            String input = scanner.nextLine(); // Read user input.
            try {
                integerValue = Integer.parseInt(input); // Attempt to parse the input as an integer.
                break; // Exit the loop if parsing is successful.
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please provide a numeric value: "); // Notify user of invalid input.
            }
        }
        return integerValue; // Return the validated integer input.
    }
}
