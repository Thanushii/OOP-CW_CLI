package configuration;

import java.io.*;

public class Configuration {
    private int totalTickets; // Total number of tickets available in the system.
    private int ticketReleaseRate; // The rate at which vendors release tickets.
    private int customerRetrievalRate; // The rate at which customers retrieve tickets.
    private int maxTicketCapacity; // The maximum capacity of tickets the pool can hold at a time.

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets; // Initialize total tickets.
        this.ticketReleaseRate = ticketReleaseRate; // Initialize ticket release rate.
        this.customerRetrievalRate = customerRetrievalRate; // Initialize customer retrieval rate.
        this.maxTicketCapacity = maxTicketCapacity; // Initialize maximum ticket capacity.
    }

    // Getter for totalTickets.
    public int getTotalTickets() { return totalTickets; }
    // Getter for ticketReleaseRate.
    public int getTicketReleaseRate() { return ticketReleaseRate; }
    // Getter for customerRetrievalRate.
    public int getCustomerRetrievalRate() { return customerRetrievalRate; }
    // Getter for maxTicketCapacity.
    public int getMaxTicketCapacity() { return maxTicketCapacity; }

    // Save the configuration to a specified file.
    public void saveToFile(String filename) {
        try (Writer writer = new FileWriter(filename)) {
            writer.write("Total Tickets: " + totalTickets + "\n"); // Save total tickets.
            writer.write("Ticket Release Rate: " + ticketReleaseRate + " \n"); // Save ticket release rate.
            writer.write("Customer Retrieval Rate: " + customerRetrievalRate + " \n"); // Save customer retrieval rate.
            writer.write("Maximum Ticket Capacity: " + maxTicketCapacity + "\n"); // Save maximum ticket capacity.
            System.out.println("Configuration is successfully saved to " + filename); // Confirm save success.
        } catch (IOException e) {
            System.err.println("An error occurred while saving the configuration." + e.getMessage()); // Handle file I/O errors.
        }
    }

    // Load a configuration from a specified file.
    public static Configuration loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read and parse the totalTickets value.
            int totalTickets = Integer.parseInt(reader.readLine().split(": ")[1]);
            // Read and parse the ticketReleaseRate value.
            int ticketReleaseRate = Integer.parseInt(reader.readLine().split(": ")[1].split(" ")[0]);
            // Read and parse the customerRetrievalRate value.
            int customerRetrievalRate = Integer.parseInt(reader.readLine().split(": ")[1].split(" ")[0]);
            // Read and parse the maxTicketCapacity value.
            int maxTicketCapacity = Integer.parseInt(reader.readLine().split(": ")[1]);

            return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity); // Return loaded configuration.
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load the configuration, " + e.getMessage() + ". "); // Handle errors during file read or parsing.
            return null; // If loading fails, return null
        }
    }
}
