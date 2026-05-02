import com.masroofy.controller.ExpenseController;

public class Main {
    public static void main(String[] args) {

        ExpenseController controller = new ExpenseController();

        // Create Budget
        controller.createBudget(1000, "2026-04-01", "2026-04-30");

        // Add Expense
        controller.addExpense(200, 1);

        // Edit
        controller.editTransaction(1, 300, 2);

        // Delete
        controller.deleteTransaction(1);

        // Get Safe Daily Limit
        double safe = controller.getSafeDailyLimit(1000, "2026-04-30");

        System.out.println("Safe Daily Limit: " + safe);
    }
}