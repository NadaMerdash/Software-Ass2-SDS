package com.masroofy.controller;

import java.util.Date;
import java.util.List;

import com.masroofy.dao.SQLiteDatabase;
import com.masroofy.model.Transaction;
import com.masroofy.service.BudgetManager;
import com.masroofy.service.InputValidation;
import com.masroofy.service.NotificationService;
import com.masroofy.service.TransactionManager;

/**
 * Controller layer for handling user interactions and coordinating between
 * the view, service layer, and data access layer.
 * <p>
 * This class manages user authentication, expense tracking, budget management,
 * and user notifications for the Masroofy application.
 * </p>
 *
 * @author Nada
 * @version 1.0
 */
public class ExpenseController {

    TransactionManager tm = new TransactionManager();
    BudgetManager bm = new BudgetManager();
    InputValidation validation = new InputValidation();
    NotificationService notification = new NotificationService();
    private SQLiteDatabase db = SQLiteDatabase.getInstance();
    private int currentUserId = -1;

    /**
     * Handles user login or registration.
     * <p>
     * If the user exists, validates the PIN. If the user is new, creates an account.
     * Sets the current user ID upon successful authentication.
     * </p>
     *
     * @param userId the user ID
     * @param pin the PIN (4 digits)
     * @return true if login/registration is successful, false otherwise
     */
    public boolean loginOrRegister(int userId, int pin) {
        if (!db.userExists(userId)) {
            boolean saved = db.saveUser(userId, pin);
            if (saved) { 
                currentUserId = userId; 
                return true; 
            }
            return false;
        } else {
            boolean valid = db.validateUser(userId, pin);
            if (valid) { 
                currentUserId = userId; 
                return true; 
            }
            return false;
        }
    }

    /**
     * Checks if a user is new (does not exist in the system).
     *
     * @param userId the user ID to check
     * @return true if the user is new, false if the user exists
     */
    public boolean isNewUser(int userId) {
        return !db.userExists(userId);
    }

    /**
     * Retrieves the current logged-in user ID.
     *
     * @return the current user ID, or -1 if no user is logged in
     */
    public int getCurrentUserId() { 
        return currentUserId; 
    }

    /**
     * Retrieves the stored PIN for the current user.
     *
     * @return the stored PIN
     */
    public int getStoredPin() {
        return db.getStoredPin(currentUserId);
    }

    /**
     * Adds a new expense transaction for the current user.
     * <p>
     * Creates a transaction, saves it, and recalculates the daily limit.
     * </p>
     *
     * @param amount the expense amount
     * @param category the expense category ID (1-7)
     */
    public void addExpense(double amount, int category) {
        String today = java.time.LocalDate.now().toString();

        Transaction transaction =
                new Transaction(currentUserId, amount, category, today);

        tm.addExpense(transaction);
        bm.reCalculateSafeDailyLimit(currentUserId);
    }

    /**
     * Edits an existing transaction.
     * <p>
     * Updates the transaction with new amount and category, then recalculates the daily limit.
     * </p>
     *
     * @param transactionId the ID of the transaction to edit
     * @param amount the new expense amount
     * @param category the new category ID (1-7)
     */
    public void editTransaction(int transactionId,
                            double amount,
                            int category) {

        String today = java.time.LocalDate.now().toString();

        Transaction transaction =
                new Transaction(
                        transactionId,
                        currentUserId,
                        amount,
                        category,
                        today
                );

        tm.editTransaction(transaction);
        bm.reCalculateSafeDailyLimit(currentUserId);
    }

    /**
     * Deletes a transaction and recalculates the daily limit.
     *
     * @param id the transaction ID to delete
     */
    public void deleteTransaction(int id) {
        tm.deleteTransaction(id);
        bm.reCalculateSafeDailyLimit(currentUserId);
    }

    /**
     * Retrieves all transactions for the current user.
     * <p>
     * Returns a list of transactions in the format [ID, Amount, Category, Date].
     * </p>
     *
     * @return a list of string arrays containing transaction details
     */
    public List<String[]> getAllTransactions() {
        return tm.getAllTransactions(currentUserId);
    }

    /**
     * Creates a new budget for the current user.
     *
     * @param allowance the budget allowance amount
     * @param start the start date in YYYY-MM-DD format
     * @param end the end date in YYYY-MM-DD format
     */
    public void createBudget(double allowance, String start, String end) {
        bm.createBudget(currentUserId, allowance, start, end);
    }

    /**
     * Retrieves the safe daily spending limit for the current user.
     *
     * @return the daily spending limit
     */
    public double getSafeDailyLimit() {
        return bm.getSafeDailyLimit(currentUserId);
    }

    /**
     * Resets the budget cycle for the current user.
     * <p>
     * Deletes all transactions and recalculates the daily limit.
     * </p>
     */
    public void resetCycle() {
        bm.resetCycle(currentUserId);
        bm.reCalculateSafeDailyLimit(currentUserId);
    }

    /**
     * Recalculates the safe daily limit for the current user.
     *
     * @return the newly calculated daily limit
     */
    public double reCalculateSafeDailyLimit() {
        return bm.reCalculateSafeDailyLimit(currentUserId);
    }

    /**
     * Validates an expense transaction.
     *
     * @param amount the expense amount
     * @param category the category ID
     * @return true if the expense is valid, false otherwise
     */
    public boolean validateExpense(double amount, int category) {
        return validation.validateExpense(amount, category);
    }

    /**
     * Validates reset operation parameters.
     *
     * @param userId the user ID
     * @param PIN the PIN provided by the user
     * @param StoredPIN the PIN stored in the system
     * @return true if reset parameters are valid, false otherwise
     */
    public boolean validateReset(int userId, int PIN, int StoredPIN) {
        return validation.ValidateReset(userId, PIN, StoredPIN);
    }

    /**
     * Validates budget parameters.
     *
     * @param Allowance the budget allowance
     * @param startDate the start date
     * @param endDate the end date
     * @return true if budget parameters are valid, false otherwise
     */
    public boolean validate(double Allowance, Date startDate, Date endDate) {
        return validation.validate(Allowance, startDate, endDate);
    }

    /**
     * Displays a notification message to the user.
     *
     * @param message the notification message
     * @return the formatted notification message
     */
    public String showNotification(String message) {
        return notification.showNotification(message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message
     * @return the formatted error message
     */
    public String showError(String message) {
        return notification.showError(message);
    }

    /**
     * Displays a confirmation message to the user.
     *
     * @param message the confirmation message
     * @return the formatted confirmation message
     */
    public String showConfirmationMessage(String message) {
        return notification.showConfirmationMessage(message);
    }
    
    /**
     * Retrieves the current budget allowance for the user.
     *
     * @return the allowance amount
     */
    public double getAllowance() {
        return bm.getAllowance(currentUserId);
    }

    /**
     * Retrieves the remaining balance in the current budget.
     *
     * @return the remaining balance
     */
    public double getRemainingBalance() {
        return bm.getRemainingBalance(currentUserId);
    }

    /**
     * Retrieves the number of remaining days in the current budget cycle.
     *
     * @return the number of remaining days
     */
    public int getRemainingDays() {
        return bm.getRemainingDays(currentUserId);
    }

    /**
     * Calculates the percentage of budget spent.
     *
     * @return the spending percentage as a string (e.g., "45.5%")
     */
    public String calculatePercentage() {
        return bm.calculatePercentage(currentUserId, getAllowance());
    }
}
