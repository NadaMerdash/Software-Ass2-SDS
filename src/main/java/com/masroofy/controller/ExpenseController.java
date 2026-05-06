package com.masroofy.controller;

import com.masroofy.service.TransactionManager;
import com.masroofy.service.BudgetManager;

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

    public void createBudget(double allowance, String start, String end) {
        bm.createBudget(allowance, start, end);
    }

    public double getSafeDailyLimit(int userID) {
        return bm.getSafeDailyLimit(userID);
    }
    public void resetCycle(int userID) {
    bm.resetCycle(userID);
}
}