package com.masroofy.service;

import java.util.List;

import com.masroofy.dao.SQLiteDatabase;
import com.masroofy.model.Transaction;

/**
 * Handles business logic related to transactions in the Masroofy system.
 * Acts as a middle layer between controller and database.
 */
public class TransactionManager {

    SQLiteDatabase db = SQLiteDatabase.getInstance();

    /**
     * Adds a new expense transaction to the database.
     *
     * @param transaction the transaction object to be saved
     */
    public void addExpense(Transaction transaction) {
        db.saveTransaction(transaction);
    }

    /**
     * Retrieves all transactions for a specific user.
     *
     * @param userId the user ID
     * @return list of transactions as string arrays
     */
    public List<String[]> getAllTransactions(int userId) {
        return db.getAllTransactions(userId);
    }

    /**
     * Updates an existing transaction.
     *
     * @param transaction the updated transaction object
     */
    public void editTransaction(Transaction transaction) {
        db.updateTransaction(transaction);
    }

    /**
     * Deletes a specific transaction by ID.
     *
     * @param id transaction ID
     */
    public void deleteTransaction(int id) {
        db.deleteTransaction(id);
    }

    /**
     * Deletes all transactions for a specific user.
     *
     * @param userID user ID
     */
    public void deleteAllTransactions(int userID) {
        db.deleteAllTransactions(userID);
    }
}
