import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ExpensesTracker {
    private static final String FILE_NAME = "expenses.txt";
    private static List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        loadExpenses();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nExpense Tracker Menu:");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. View Expenses by Category");
            System.out.println("4. Total Amount Spent");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addExpense(scanner);
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    viewExpensesByCategory(scanner);
                    break;
                case 4:
                    totalAmountSpent();
                    break;
                case 5:
                    saveExpenses();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        LocalDate date = LocalDate.now();
        Category category = chooseCategory(scanner);
        Expense expense = new Expense(description, amount, date, category);
        expenses.add(expense);
        System.out.println("Expense added.");
    }

    private static Category chooseCategory(Scanner scanner) {
        System.out.println("Choose category:");
        for (Category category : Category.values()) {
            System.out.println(category.ordinal() + 1 + ". " + category);
        }
        int categoryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return Category.values()[categoryChoice - 1];
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
        }
    }

    private static void viewExpensesByCategory(Scanner scanner) {
        Category category = chooseCategory(scanner);
        boolean found = false;
        for (Expense expense : expenses) {
            if (expense.getCategory() == category) {
                System.out.println(expense);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No expenses recorded for this category.");
        }
    }

    private static void totalAmountSpent() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        System.out.println("Total amount spent: $" + total);
    }

    private static void loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            expenses = (List<Expense>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, start with an empty list
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveExpenses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(expenses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

enum Category {
    FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, OTHER
}

class Expense implements Serializable {
    private String description;
    private double amount;
    private LocalDate date;
    private Category category;

    public Expense(String description, double amount, LocalDate date, Category category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Description: " + description + ", Amount: $" + amount + ", Category: " + category;
    }
}
