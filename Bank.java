package PROJECT;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

interface BankOperations {
    void deposit(double amount);
    void withdraw(double amount);
    void showBalance();
}

abstract class Account implements BankOperations {
    String accNumber;
    double balance;

    Account(String accNumber, double balance) {
        this.accNumber = accNumber;
        this.balance = balance;
    }

   
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return;
        }
        balance += amount;
        System.out.println("Amount Deposited: " + amount);
    }

   
    public void showBalance() {
        System.out.println("Account: " + accNumber + " | Current Balance: " + balance);
    }
}

class SavingsAccount extends Account {
    private double interestRate = 0.05; 

    SavingsAccount(String accNumber, double balance) {
        super(accNumber, balance);
    }

   
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdraw amount must be positive.");
            return;
        }

        if (amount <= balance) {
            balance -= amount;
            System.out.println("Amount Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient Balance!");
        }
    }

    public void calculateInterest() {
        double interest = balance * interestRate;
        System.out.println("Interest Added: " + interest);
        balance += interest;
    }
}

class CurrentAccount extends Account {

    CurrentAccount(String accNumber, double balance) {
        super(accNumber, balance);
    }

   
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdraw amount must be positive.");
            return;
        }
        
        balance -= amount;
        System.out.println("Amount Withdrawn: " + amount);
    }
}

class LoanAccount extends Account {
    LoanAccount(String accNumber, double balance) {
        super(accNumber, balance);
    }

    
    public void withdraw(double amount) {
        System.out.println("Withdraw not allowed in Loan Account.");
    }

    public void payEMI(double amount) {
        if (amount <= 0) {
            System.out.println("EMI amount must be positive.");
            return;
        }
        balance -= amount; 
        System.out.println("EMI Paid: " + amount);
    }
}

class BankService {

    public void transfer(Account from, Account to, double amount) {
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return;
        }
        if (from.balance >= amount) {
            from.deposit(-amount); 
            to.deposit(amount);
            System.out.println("Transfer Successful: " + amount);
        } else {
            System.out.println("Transfer Failed! Not enough balance.");
        }
    }
}

class FileManager {

    public static void saveData(Account acc) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("data3.txt", true));
            bw.write(acc.accNumber + " - Balance: " + acc.balance + System.lineSeparator());
            bw.flush();
            System.out.println("Data saved to file (data3.txt).");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ignored) {}
            }
        }
    }
}

public class Bank {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        
        Account a = new SavingsAccount("AC1001", 1000.0);
        BankService service = new BankService();

        while (true) {
            System.out.println("\n--- SMART BANKING SIMULATOR ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Balance");
            System.out.println("4. Add Interest (Savings only)");
            System.out.println("5. Save to File");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = s.nextInt();
            } catch (InputMismatchException ime) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                s.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    try {
                        double amt = s.nextDouble();
                        a.deposit(amt);
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid amount.");
                        s.nextLine();
                    }
                    break;

                case 2:
                    System.out.print("Enter amount: ");
                    try {
                        double amt = s.nextDouble();
                        a.withdraw(amt);
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid amount.");
                        s.nextLine();
                    }
                    break;

                case 3:
                    a.showBalance();
                    break;

                case 4:
                    if (a instanceof SavingsAccount) {
                        ((SavingsAccount) a).calculateInterest();
                    } else {
                        System.out.println("Interest can only be added to a Savings Account.");
                    }
                    break;

                case 5:
                    FileManager.saveData(a);
                    break;

                case 6:
                    System.out.println("Thank you!");
                    s.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please select 1-6.");
            }
        }
    }
}
