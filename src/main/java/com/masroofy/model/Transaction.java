package com.masroofy. model;

public class Transaction {
    private int id;
    private double amount;
    private int categoryId;
    private String date;

    public Transaction(int id, double amount, int categoryId, String date) {
        this.id = id;
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
    }
    public int getId() {
        return id;
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
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public void setDate(String date) {
        this.date = date;
    }
}