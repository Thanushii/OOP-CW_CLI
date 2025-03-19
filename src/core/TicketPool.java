package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {
    private final List<String> tickets = Collections.synchronizedList(new ArrayList<>()); // Thread-safe list of tickets.
    private final int max_capacity; // Maximum tickets the pool can hold at any moment.
    private final int totalTickets; // Total tickets available through the system.
    private int tickets_produced = 0; // Counter for tickets produced upto present.
    private int tickets_purchased = 0; // Counter for tickets purchased upto present.
    private final String logFile = "system.log"; // File to store the system logs.

    public TicketPool(int maxCapacity, int totalTickets) {
        this.max_capacity = maxCapacity; // Initialize maxCapacity.
        this.totalTickets = totalTickets; // Initialize totalTickets.
    }

    // Add tickets to the pool, making sure of synchronization and constraints.
    public synchronized void addTickets(String ticket, int quantity) {
        // If the pool is full or total tickets have been produced, wait.
        while (tickets.size() + quantity > max_capacity || tickets_produced >= totalTickets) {
            if (tickets_produced >= totalTickets) {
                notifyAll(); // Inform waiting threads that production is complete.
                return; // Stop adding tickets.
            }
            try {
                String message = "Ticket pool is full. Vendor is now waiting."; // Log pool full status.
                System.out.println(message);
                logToFile(message);
                wait(); // Wait until tickets are consumed.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status.
                String error = "The thread was interrupted while trying to add tickets." + e.getMessage();
                System.err.println(error);
                logToFile(error);
                return;
            }
        }

        // Add tickets to the pool.
        for (int i = 0; i < quantity && tickets_produced < totalTickets; i++) {
            tickets.add(ticket + "-" + (tickets_produced + 1)); // Generate unique ticket ID.
            tickets_produced++;
        }

        String message = "Successfully added " + quantity + " tickets. The pool now contains a total of " + tickets.size();
        System.out.println(message);
        logToFile(message);
        notifyAll(); // Inform waiting threads that tickets are available.
    }

    // Retrieve a ticket from the pool, making sure of synchronization and constraints.
    public synchronized String retrieveTicket() {
        // If the pool is empty, wait.
        while (tickets.isEmpty()) {
            if (tickets_purchased >= totalTickets) {
                notifyAll(); // Inform waiting threads that all tickets are sold.
                return null; // Stop retrieval as all tickets are purchased.
            }
            try {
                String message = "No tickets are currently available. Customer is waiting. ";
                System.out.println(message);
                logToFile(message);
                wait(); // Wait until tickets are added.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status.
                String error = "The thread was interrupted while attempting to retrieve tickets. " + e.getMessage();
                System.err.println(error);
                logToFile(error);
                return null;
            }
        }

        // Retrieve and remove a ticket from the pool.
        String ticket = tickets.remove(0);
        tickets_purchased++;
        String message = "Ticket successfully retrieved. " + ticket + "Remaining tickets in the pool: " + tickets.size();
        System.out.println(message);
        logToFile(message);

        // End program if all the tickets are purchased.
        if (tickets_purchased >= totalTickets) {
            String completionMessage = "All tickets have been sold. The program will now terminate. ";
            System.out.println(completionMessage);
            logToFile(completionMessage);
            System.exit(0);
        }

        notifyAll(); // Inform the threads that tickets have been consumed.
        return ticket; // Return the retrieved ticket.
    }

    // Log messages to a file for auditing or debugging.
    public void logToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(message); // Write message to log file.
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage()); // Handle file I/O errors.
        }
    }
}
