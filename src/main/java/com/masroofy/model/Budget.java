/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.masroofy.model;

/**
 *
 * @author Nada
 */
public class Budget {
    
    private int userID;
    private double allowance;
    private String startDate;
    private String endDate;
    private double dailyLimit;

    public Budget(double allowance, String startDate, String endDate) {
        this.allowance = allowance;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Budget(int userID, double allowance, String startDate, String endDate, double dailyLimit) {
        this.userID = userID;
        this.allowance = allowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyLimit = dailyLimit;

    }

    public int getUserID() {
        return userID;
    }
    
    public double getAllowance() {
        return allowance;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
    
    public double getDailyLimit() {
        return dailyLimit;
    }

}