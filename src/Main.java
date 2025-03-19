import configuration.Configuration; // Import Configuration class for managing application settings.
import core.TicketPool; // Import TicketPool class for ticket management logic.
import threads.Customer; // Import Customer class for customer behavior implementation.
import threads.Vendor; // Import Vendor class for vendor behavior implementation.
import validation.validation; // Import validation class for input validation utilities.

import java.util.Scanner; // Import Scanner for reading user input.

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create a Scanner instance for user input.
        Configuration config = null; // Initialize a Configuration object to store settings.

        // Ask the user if they want to load an existing configuration or create a new one.
        System.out.println("Would you like to load a pre-existing configuration? (yes/no)");
        String choice = scanner.nextLine().trim().toLowerCase(); // Get user input and normalize case.

        if (choice.equals("yes")) { // If user chooses to load a configuration file.
            System.out.println("Please enter the name of the configuration file:"); // Prompt for file name.
            String filename = scanner.nextLine(); // Read file name input.
            config = Configuration.loadFromFile(filename); // Attempt to load configuration from the file.
            if (config == null) { // If loading fails, notify user and create a new configuration.
                System.out.println("Failed to load the configuration. A new one will be created.");
                config = MakeNewConfiguration(scanner);
            }
        } else { // If user opts not to load a file, create a new configuration.
            config = MakeNewConfiguration(scanner);
        }

        // Validate that the pool's max capacity is less than the total tickets available.
        if (config.getMaxTicketCapacity() >= config.getTotalTickets()) {
            System.err.println("Invalid input: The pool's capacity should be smaller than the total number of tickets.");
            return; // Exit program if validation fails.
        }

        // Initialize the TicketPool with configuration values.
        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

        // Create two Vendor threads and assign them to the TicketPool.
        Vendor vendor1 = new Vendor(ticketPool, config.getTicketReleaseRate(), "Vendor1");
        Vendor vendor2 = new Vendor(ticketPool, config.getTicketReleaseRate(), "Vendor2");
        Thread vendorThread1 = new Thread(vendor1); // Wrap Vendor1 in a Thread object.
        Thread vendorThread2 = new Thread(vendor2); // Wrap Vendor2 in a Thread object.

        // Create two Customer threads and assign them to the TicketPool.
        Customer customer1 = new Customer(ticketPool, config.getCustomerRetrievalRate(), "Customer1");
        Customer customer2 = new Customer(ticketPool, config.getCustomerRetrievalRate(), "Customer2");
        Thread customerThread1 = new Thread(customer1); // Wrap Customer1 in a Thread object.
        Thread customerThread2 = new Thread(customer2); // Wrap Customer2 in a Thread object.

        // Start all threads to begin ticket production and consumption.
        vendorThread1.start();
        vendorThread2.start();
        customerThread1.start();
        customerThread2.start();
    }

    private static Configuration MakeNewConfiguration(Scanner scanner) {
        // Prompt for the total ticket capacity from the user.
        int totalTickets = validation.AskForInput(scanner, "Enter the total number of tickets:");

        // Prompt for the ticket release rate for vendors.
        int ticketReleaseRate = validation.AskForInput(scanner, "Enter the rate at which vendors release tickets:");

        // Prompt for the ticket retrieval rate for customers.
        int customerRetrievalRate = validation.AskForInput(scanner, "Enter the rate at which customers retrieve tickets");

        int maxTicketCapacity; // Initialize variable for maximum ticket capacity.
        do {
            // Prompt user for max ticket pool capacity and validate it.
            maxTicketCapacity = validation.AskForInput(scanner, "Enter maximum ticket capacity of the pool:");
            if (!validation.isMaximumCapacityValid(maxTicketCapacity, totalTickets)) {
                System.err.println("Invalid input: The pool's capacity cannot exceed the total number of tickets.");
            }
        } while (!validation.isMaximumCapacityValid(maxTicketCapacity, totalTickets)); // Repeat until valid input.

        // Create a Configuration object with the collected inputs.
        Configuration config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        // Ask if the user wants to save the configuration to a file.
        System.out.println("Would you like to save this configuration? (yes/no)");
        String saveChoice = scanner.nextLine().trim().toLowerCase(); // Read save decision.
        if (saveChoice.equals("yes")) { // If user chooses to save configuration.
            System.out.println("Provide the name of the file to save the configuration:");
            String filename = scanner.nextLine(); // Get the filename.
            config.saveToFile(filename); // Save the configuration to the specified file.
        }
        return config; // Return the created Configuration object.
    }
}
