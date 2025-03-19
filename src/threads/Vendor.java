package threads;

import core.TicketPool;

public class Vendor implements Runnable {
    private final TicketPool ticket_pool; // Reference to the shared TicketPool.
    private final int ticketReleaseRate; // The rate at which the vendor releases tickets (tickets/sec).
    private final String vendor_name; // Name of the vendor for identification.

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, String vendorName) {
        this.ticket_pool = ticketPool; // Initialize the TicketPool reference.
        this.ticketReleaseRate = ticketReleaseRate; // Initialize the ticket release rate.
        this.vendor_name = vendorName; // Initialize the vendor name.
    }

    @Override
    public void run() {
        while (true) { // Infinite loop for continuous ticket release.
            try {
                Thread.sleep(1000 / ticketReleaseRate); // Wait based on the release rate.
                String ticketType = vendor_name + " - Ticket"; // Generate a ticket type using the vendor's name.
                ticket_pool.addTickets(ticketType, 1); // Add a single ticket to the TicketPool.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status.
                String error = "Vendor " + vendor_name + " was interrupted: " + e.getMessage(); // Prepare error message.
                System.err.println(error);
                ticket_pool.logToFile(error); // Log the error message.
                break; // Exit the loop on interruption.
            }
        }
    }
}
