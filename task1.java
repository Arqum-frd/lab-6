package lab6;

class BankAccount {
    int balance;

    // Constructor to initialize the bank account with a balance
    public BankAccount(int balance) {
        this.balance = balance;
    }

    // Synchronized method to handle withdrawals
    public synchronized void withdraw(String user, int amount) {
        if (balance >= amount) {
            System.out.println(user + " is withdrawing " + amount);
            balance -= amount;
            System.out.println("Account balance after " + user + "'s withdrawal: " + balance);
        } else {
            System.out.println("Insufficient funds for " + user + ". Balance: " + balance + ", Attempted withdrawal: " + amount);
        }
    }
}

class WithdrawalTask implements Runnable {
    private BankAccount account;
    private String user;
    private int amount;

    public WithdrawalTask(BankAccount account, String user, int amount) {
        this.account = account;
        this.user = user;
        this.amount = amount;
    }

    @Override
    public void run() {
        account.withdraw(user, amount);
    }
}

public class task1 {
    public static void main(String[] args) {
        // Create a bank account with an initial balance of 50,000
        BankAccount account = new BankAccount(50000);

        // Create two threads for User A and User B
        Thread userA = new Thread(new WithdrawalTask(account, "User A", 45000));
        Thread userB = new Thread(new WithdrawalTask(account, "User B", 20000));

        // Start the threads
        userA.start();
        userB.start();

        // Wait for both threads to finish
        try {
            userA.join();
            userB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final account balance
        System.out.println("Final account balance: " + account.balance);
    }
}
