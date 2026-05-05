package com.masroofy.service;

import com.masroofy.dao.SQLiteDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// Service Layer
public class BudgetManager {

    SQLiteDatabase db = SQLiteDatabase.getInstance();
    private TransactionManager transactionManager ;
    private NotificationService notificationService;
    

    public long calculateDaysBetween(String firstDate, String lastDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(firstDate, formatter);
        LocalDate endDate = LocalDate.parse(lastDate, formatter);

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        return daysBetween;
    }

    // Sequence: calculateSafeDailyLimit
    public double calculateSafeDailyLimit(double allowance, long days) {
        if (days == 0) return 0;
        return allowance / days;
    }

    // Sequence: Create Budget
    public void createBudget(double allowance, String start, String end) {
        long days =calculateDaysBetween(start,end);
        double dailyLimit = calculateSafeDailyLimit(allowance, days);
        db.saveBudget(allowance, start, end, dailyLimit);
    }

    // Sequence: get safe Daily Limit
    public double getSafeDailyLimit(double allowance, String endDate) {
        double balance = db.getRemainingBalance(allowance);
        int days = db.getRemainingDays(endDate);

        double safe = calculateSafeDailyLimit(balance, days);

        db.saveSafeDailyLimit(safe);

        return safe;
    }
    
    public double reCalculateSafeDailyLimit(int userID) {
        int days= db.getRemainingDays(userID);
        double balance = db.getRemainingBalance(userID);
        double newDailyLimit = calculateSafeDailyLimit(balance, days);
        db.saveSafeDailyLimit(balance);
        return newDailyLimit ;
    }
    
    public String calculatePercentage(int userID, double totalBudget){
        double totalSpent= db.getTotalExpenses(userID);
        return (totalSpent/totalBudget)*100 +"%";
    }
    
    public void resetCycle(int userID){
        transactionManager.deleteTransaction(userID);
        notificationService.showNotification("Rest Done Successfuly");
        
    }
}