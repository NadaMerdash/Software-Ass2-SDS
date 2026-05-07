package com.masroofy. model;

public class Transaction {
    private int transactionId;
    private int userId;
    private double amount;
    private int categoryId;
    private String date;

    public Transaction(int userId, double amount, int categoryId, String date) {
        this.userId = userId;
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
    }
    
    public Transaction(int transactionId,int userId, double amount, int categoryId, String date) {

    this.transactionId = transactionId;
    this.userId = userId;
    this.amount = amount;
    this.categoryId = categoryId;
    this.date = date;
    }

    public int getUserId() {
    return userId;
    }
    
    public int getTransactionId() {
    return transactionId;
}

    public double getAmount() {
        return amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDate() {
        return date;
    }
}