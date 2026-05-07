package com.masroofy.service;

import java.util.List;

import com.masroofy.dao.SQLiteDatabase;

// Service Layer - Business Logic
public class TransactionManager {

    SQLiteDatabase db = SQLiteDatabase.getInstance();

   
    public void addExpense(int userId, double amount, int category, String date) {
        db.saveTransaction(userId, amount, category, date);
    }
     public List<String[]> getAllTransactions(int userId) {
        return db.getAllTransactions(userId);
    }

    // Sequence: Edit Transaction
    public void editTransaction(int id, double amount, int category) {
        db.updateTransaction(id, amount, category);
    }

    // Sequence: Delete Transaction
    public void deleteTransaction(int id) {
        db.deleteTransaction(id);
    }
    public void deleteAllTransactions(int userID) {
        db.deleteAllTransactions(userID);
    }
}