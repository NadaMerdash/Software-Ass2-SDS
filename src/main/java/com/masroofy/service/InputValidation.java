package com.masroofy.service;

import java.util.Date;

/**
 * Provides validation logic for user inputs in the Masroofy system.
 * Includes validation for budgets, expenses, and reset operations.
 */
public class InputValidation {

    NotificationService notificationService = new NotificationService();

    /**
     * Validates budget input data.
     *
     * @param Allowance total budget allowance
     * @param startDate budget start date
     * @param endDate budget end date
     * @return true if data is valid, otherwise false
     */
    public boolean validate(double Allowance, Date startDate, Date endDate) {
        if (Allowance <= 0) {
            notificationService.showError("Allowance must be a positive number.");
            return false;
        }
        if (startDate == null || endDate == null) {
            notificationService.showError("Start date and end date cannot be null.");
            return false;
        }
        if (startDate.after(endDate)) {
            notificationService.showError("Start date must be before end date.");
            return false;
        }
        return true;
    }

    /**
     * Validates an expense input.
     *
     * @param amount expense amount
     * @param category category ID (1 to 7)
     * @return true if valid, otherwise false
     */
    public boolean validateExpense(double amount, int category) {
        if (amount <= 0) {
            notificationService.showError("Expense amount must be a positive number.");
            return false;
        }
        if (category < 1 || category > 7) {
            notificationService.showError("Invalid expense category.");
            return false;
        }
        return true;
    }

    /**
     * Validates reset operation using user credentials.
     *
     * @param userID user ID
     * @param PIN entered PIN
     * @param StoredPIN stored PIN in database
     * @return true if reset is allowed, otherwise false
     */
    public boolean ValidateReset(int userID, int PIN, int StoredPIN) {
        if (userID <= 0) {
            notificationService.showError("Invalid user ID.");
            return false;
        }
        if (PIN < 1000 || PIN > 9999) {
            notificationService.showError("PIN must be 4 digits.");
            return false;
        }
        if (PIN != StoredPIN) {
            notificationService.showError("Incorrect PIN.");
            return false;
        }
        return true;
    }
}
