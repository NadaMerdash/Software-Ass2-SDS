package com.masroofy.view;

import com.masroofy.dao.SQLiteDatabase;

// Service Layer - Business Logic
public class TransactionManager {

    SQLiteDatabase db = SQLiteDatabase.getInstance();

    // Sequence: addExpense → saveTransaction
    public void addExpense(double amount, int category) {
        db.saveTransaction(amount, category, "2026-04-30");
    }

    // Sequence: Edit Transaction
    public void editTransaction(int id, double amount, int category) {
        db.updateTransaction(id, amount, category);
    }

    // Sequence: Delete Transaction
    public void deleteTransaction(int id) {
        db.deleteTransaction(id);
    }
}