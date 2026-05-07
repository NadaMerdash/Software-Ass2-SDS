package com.masroofy.controller;

import java.util.List;

import com.masroofy.dao.SQLiteDatabase;
import com.masroofy.service.BudgetManager;
import com.masroofy.service.TransactionManager;

// Controller Layer
public class ExpenseController {

    TransactionManager tm = new TransactionManager();
    BudgetManager bm = new BudgetManager();
     private SQLiteDatabase db = SQLiteDatabase.getInstance();
      private int currentUserId = -1;

       public boolean loginOrRegister(int userId, int pin) {
        if (!db.userExists(userId)) {
            boolean saved = db.saveUser(userId, pin);
            if (saved) { currentUserId = userId; return true; }
            return false;
        } else {
            boolean valid = db.validateUser(userId, pin);
            if (valid) { currentUserId = userId; return true; }
            return false;
        }
    }
    public boolean isNewUser(int userId) {
        return !db.userExists(userId);
    }
    public int getCurrentUserId() { 
        return currentUserId; 
    }
     public int getStoredPin() {
        return db.getStoredPin(currentUserId);
    }

    public void addExpense(double amount, int category) {
        String today = java.time.LocalDate.now().toString();
        tm.addExpense(currentUserId, amount, category, today);
    }


    public void editTransaction(int id, double amount, int category) {
        tm.editTransaction(id, amount, category);
    }

    public void deleteTransaction(int id) {
        tm.deleteTransaction(id);
    }
    public List<String[]> getAllTransactions() {
        return tm.getAllTransactions(currentUserId);
    }

     public void createBudget(double allowance, String start, String end) {
        bm.createBudget(currentUserId, allowance, start, end);
    }

     public double getSafeDailyLimit() {
        return bm.getSafeDailyLimit(currentUserId);
    }
     public void resetCycle() {
        bm.resetCycle(currentUserId);
    }
}