package lab6;
class PrinterJob {
    private int pagesInTray; // The number of pages currently in the tray
    
    // Constructor to initialize the number of pages in the tray
    public PrinterJob(int totalPages) {
        this.pagesInTray = totalPages;
    }

    // Method for the Printer thread to print pages
    public synchronized void printPages(int pagesToPrint) {
        try {
            System.out.println("Printer thread: Waiting to print " + pagesToPrint + " pages...");
            
            // Wait until there are enough pages in the tray
            while (pagesInTray < pagesToPrint) {
                System.out.println("Printer thread: Not enough pages, waiting...");
                wait(); // This causes the thread to wait
            }
            
            // Once enough pages are available, print the pages
            pagesInTray -= pagesToPrint;
            System.out.println("Printer thread: Printing " + pagesToPrint + " pages. Pages left in tray: " + pagesInTray);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method for the Page Calculation thread to add pages to the tray
    public synchronized void calculateRemainingPages(int pagesAdded) {
        try {
            System.out.println("Page calculation thread: Adding " + pagesAdded + " pages to tray.");
            pagesInTray += pagesAdded;
            System.out.println("Page calculation thread: " + pagesInTray + " pages now in tray.");
            
            // Notify the printer thread that pages are available
            notifyAll(); // This wakes up all threads waiting on this object (including printer thread)
            System.out.println("Page calculation thread: Notified printer thread.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class task2 {

    public static void main(String[] args) {
        // Create a PrinterJob object with 10 pages in the tray initially
        PrinterJob printerJob = new PrinterJob(10);

        // Thread for the Printer to print pages
        Thread printerThread = new Thread(() -> {
            printerJob.printPages(15);  // Trying to print 15 pages
        });

        // Thread for the Page Calculation to add pages to the tray
        Thread pageCalculationThread = new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate a delay in page calculation
                printerJob.calculateRemainingPages(10); // Adding 10 pages to the tray
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start both threads
        printerThread.start();
        pageCalculationThread.start();

        // Wait for both threads to finish
        try {
            printerThread.join();
            pageCalculationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Job complete.");
    }
}


