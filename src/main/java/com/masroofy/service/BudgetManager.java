package com.masroofy.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.masroofy.dao.SQLiteDatabase;
import com.masroofy.model.Budget;

/**
 * Handles all budget-related business logic in the Masroofy system.
 * Responsible for budget creation, daily limit calculation,
 * remaining balance, remaining days, and budget reset operations.
 */
public class BudgetManager {

    SQLiteDatabase db = SQLiteDatabase.getInstance();
    private TransactionManager transactionManager = new TransactionManager();
    private NotificationService notificationService = new NotificationService();

    /**
     * Calculates number of days between two dates.
     *
     * @param firstDate start date in format yyyy-MM-dd
     * @param lastDate end date in format yyyy-MM-dd
     * @return number of days between the two dates
     */
    public long calculateDaysBetween(String firstDate, String lastDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(firstDate, formatter);
        LocalDate endDate = LocalDate.parse(lastDate, formatter);

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        return daysBetween;
    }

    /**
     * Calculates safe daily spending limit.
     *
     * @param allowance total budget amount
     * @param days number of days in budget period
     * @return safe daily limit
     */
    public double calculateSafeDailyLimit(double allowance, long days) {
        if (days == 0) return 0;
        return allowance / days;
    }

    /**
     * Creates a new budget for a user.
     *
     * @param userID user identifier
     * @param allowance total budget allowance
     * @param start start date
     * @param end end date
     */
    public void createBudget(int userID, double allowance, String start, String end) {
        long days = calculateDaysBetween(start, end);
        double dailyLimit = calculateSafeDailyLimit(allowance, days);

        Budget budget = new Budget(userID, allowance, start, end, dailyLimit);
        db.saveBudget(budget);
    }

    /**
     * Retrieves stored safe daily limit for a user.
     *
     * @param userID user identifier
     * @return safe daily limit
     */
    public double getSafeDailyLimit(int userID) {
        return db.getSafeDailyLimit(userID);
    }

    /**
     * Recalculates safe daily limit based on remaining balance and days.
     *
     * @param userID user identifier
     * @return updated safe daily limit
     */
    public double reCalculateSafeDailyLimit(int userID) {
        int days = db.getRemainingDays(userID);
        double balance = db.getRemainingBalance(userID);

        double newDailyLimit = calculateSafeDailyLimit(balance, days);
        db.saveSafeDailyLimit(newDailyLimit, userID);

        return newDailyLimit;
    }

    /**
     * Calculates percentage of spent money from total budget.
     *
     * @param userID user identifier
     * @param totalBudget total budget amount
     * @return spending percentage as String
     */
    public String calculatePercentage(int userID, double totalBudget) {
        double totalSpent = db.getTotalExpenses(userID);
        return (totalSpent / totalBudget) * 100 + "%";
    }

    /**
     * Gets remaining balance for user.
     *
     * @param userID user identifier
     * @return remaining balance
     */
    public double getRemainingBalance(int userID) {
        return db.getRemainingBalance(userID);
    }

    /**
     * Gets remaining days in budget cycle.
     *
     * @param userID user identifier
     * @return remaining days
     */
    public int getRemainingDays(int userID) {
        return db.getRemainingDays(userID);
    }

    /**
     * Gets total allowance for user.
     *
     * @param userID user identifier
     * @return total allowance
     */
    public double getAllowance(int userID) {
        return db.getAllowance(userID);
    }

    /**
     * Resets budget cycle by deleting all transactions.
     *
     * @param userID user identifier
     */
    public void resetCycle(int userID) {
        transactionManager.deleteAllTransactions(userID);
        notificationService.showNotification("Reset Done Successfully");
    }
}
