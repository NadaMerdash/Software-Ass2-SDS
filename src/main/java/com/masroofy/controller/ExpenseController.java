package com.masroofy.controller;

import com.masroofy.service.TransactionManager;
import com.masroofy.model.BudgetManager;

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

    public double getSafeDailyLimit(double allowance, String endDate) {
        return bm.getSafeDailyLimit(allowance, endDate);
    }
}