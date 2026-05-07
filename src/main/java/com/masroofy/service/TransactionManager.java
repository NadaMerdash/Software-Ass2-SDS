package com.masroofy.service;

import java.util.List;

import com.masroofy.dao.SQLiteDatabase;
import com.masroofy.model.Transaction;

// Service Layer - Business Logic
public class TransactionManager {

    SQLiteDatabase db = SQLiteDatabase.getInstance();

   
    public void addExpense(Transaction transaction) {
        db.saveTransaction(transaction);
    }
     public List<String[]> getAllTransactions(int userId) {
        return db.getAllTransactions(userId);
    }

    // Sequence: Edit Transaction
    public void editTransaction(Transaction transaction) {
        db.updateTransaction(transaction);
    }

    // Sequence: Delete Transaction
    public void deleteTransaction(int id) {
        db.deleteTransaction(id);
    }
    public void deleteAllTransactions(int userID) {
        db.deleteAllTransactions(userID);
    }
}