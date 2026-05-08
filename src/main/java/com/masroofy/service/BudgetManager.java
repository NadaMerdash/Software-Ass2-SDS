package com.masroofy.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.masroofy.dao.SQLiteDatabase;
import com.masroofy.model.Budget;

// Service Layer
public class BudgetManager {

    SQLiteDatabase db = SQLiteDatabase.getInstance();
   private TransactionManager transactionManager = new TransactionManager();
   private NotificationService notificationService = new NotificationService();
    

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
     public void createBudget(int userID, double allowance, String start, String end) {
        long days = calculateDaysBetween(start, end);
        double dailyLimit = calculateSafeDailyLimit(allowance, days);
        Budget budget = new Budget(userID, allowance, start, end, dailyLimit);
        db.saveBudget(budget);
    }

    // Sequence: get safe Daily Limit
    public double getSafeDailyLimit(int userID) {
    return db.getSafeDailyLimit(userID);
}
    
    public double reCalculateSafeDailyLimit(int userID) {
        int days= db.getRemainingDays(userID);
        double balance = db.getRemainingBalance(userID);
        double newDailyLimit = calculateSafeDailyLimit(balance, days);
        db.saveSafeDailyLimit(newDailyLimit,userID);
        return newDailyLimit ;
    }
    
    public String calculatePercentage(int userID, double totalBudget){
        double totalSpent= db.getTotalExpenses(userID);
        return (totalSpent/totalBudget)*100 +"%";
    }
        public double getRemainingBalance(int userID) {
            return db.getRemainingBalance(userID);
        }
        public int getRemainingDays(int userID) {
            return db.getRemainingDays(userID);
        }
        public double getAllowance(int userID) {
            return db.getAllowance(userID);
        }

     public void resetCycle(int userID) {
        transactionManager.deleteAllTransactions(userID);
        notificationService.showNotification("Reset Done Successfully");
    }
}