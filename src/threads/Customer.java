package threads;

import core.TicketPool;

public class Customer implements Runnable {
    private final TicketPool ticket_pool; // Reference to the shared TicketPool.
    private final int customerRetrievalRate; // The rate at which the customer retrieves tickets.
    private final String customer_name; // Name of the customer for identification.

    public Customer(TicketPool ticketPool, int customerRetrievalRate, String customerName) {
        this.ticket_pool = ticketPool; // Initialize the TicketPool reference.
        this.customerRetrievalRate = customerRetrievalRate; // Initialize the retrieval rate.
        this.customer_name = customerName; // Initialize the customer name.
    }

    @Override
    public void run() {
        while (true) { // Infinite loop for continuous ticket retrieval.
            try {
                Thread.sleep(1000 / customerRetrievalRate); // Wait based on the retrieval rate.
                String ticket = ticket_pool.retrieveTicket(); // Retrieve a ticket from the TicketPool.
                if (ticket != null) {
                    // Log successful ticket retrieval.
                    String message = customer_name + " has successfully purchased ticket. " + ticket;
                    System.out.println(message);
                    ticket_pool.logToFile(message); // Log the message to the TicketPool log.
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status.
                String error = " Customer " + customer_name + " was interrupted. " + e.getMessage(); // Prepare error message.
                System.err.println(error);
                ticket_pool.logToFile(error); // Log the error message.
                break; // Exit the loop on interruption.
            }
        }
    }
}
