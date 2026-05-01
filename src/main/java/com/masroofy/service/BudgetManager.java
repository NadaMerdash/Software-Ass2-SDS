package com.masroofy.service;

import com.masroofy.dao.SQLiteDatabase;

// Service Layer
public class BudgetManager {

    SQLiteDatabase db = SQLiteDatabase.getInstance();

    // Sequence: calculateDailyLimit
    public double calculateDailyLimit(double allowance, int days) {
        if (days == 0) return 0;
        return allowance / days;
    }

    // Sequence: Create Budget Cycle
    public void createBudget(double allowance, String start, String end, int days) {
        double daily = calculateDailyLimit(allowance, days);
        db.saveBudget(allowance, start, end, daily);
    }

    // Sequence: calculateSafeDailyLimit
    public double calculateSafeDailyLimit(double balance, int days) {
        if (days == 0) return 0;
        return balance / days;
    }

    // Sequence: get Daily Limit
    public double getSafeDailyLimit(double allowance, String endDate) {
        double balance = db.getRemainingBalance(allowance);
        int days = db.getRemainingDays(endDate);

        double safe = calculateSafeDailyLimit(balance, days);

        db.saveSafeDailyLimit(safe);

        return safe;
    }
}