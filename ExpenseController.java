package controller;

import service.TransactionManager;
import service.BudgetManager;

// Controller Layer
public class ExpenseController {

    TransactionManager tm = new TransactionManager();
    BudgetManager bm = new BudgetManager();

    public void addExpense(double amount, int category) {
        tm.addExpense(amount, category);
    }

    public void editTransaction(int id, double amount, int category) {
        tm.editTransaction(id, amount, category);
    }

    public void deleteTransaction(int id) {
        tm.deleteTransaction(id);
    }

    public void createBudget(double allowance, String start, String end, int days) {
        bm.createBudget(allowance, start, end, days);
    }

    public double getSafeDailyLimit(double allowance, String endDate) {
        return bm.getSafeDailyLimit(allowance, endDate);
    }
}