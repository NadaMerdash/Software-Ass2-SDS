package com.masroofy.model;

/**
 * Represents a budget cycle for tracking and managing user expenses.
 * <p>
 * This class encapsulates budget information including the allocated allowance,
 * budget start and end dates, and the calculated daily spending limit.
 * </p>
 *
 * @author Nada
 * @version 1.0
 */
public class Budget {
    
    private int userID;
    private double allowance;
    private String startDate;
    private String endDate;
    private double dailyLimit;

    /**
     * Constructs a Budget with allowance and date range.
     * <p>
     * Creates a new budget without a user ID or daily limit.
     * Primarily used for initial budget creation.
     * </p>
     *
     * @param allowance the total budget allowance amount (in currency units)
     * @param startDate the budget start date (in YYYY-MM-DD format)
     * @param endDate the budget end date (in YYYY-MM-DD format)
     */
    public Budget(double allowance, String startDate, String endDate) {
        this.allowance = allowance;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Constructs a complete Budget with all parameters.
     * <p>
     * Creates a fully-formed budget object including user ID and calculated daily limit.
     * </p>
     *
     * @param userID the unique identifier of the user associated with this budget
     * @param allowance the total budget allowance amount (in currency units)
     * @param startDate the budget start date (in YYYY-MM-DD format)
     * @param endDate the budget end date (in YYYY-MM-DD format)
     * @param dailyLimit the calculated safe daily spending limit
     */
    public Budget(int userID, double allowance, String startDate, String endDate, double dailyLimit) {
        this.userID = userID;
        this.allowance = allowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyLimit = dailyLimit;
    }

    /**
     * Gets the user ID associated with this budget.
     *
     * @return the user ID
     */
    public int getUserID() {
        return userID;
    }
    
    /**
     * Gets the total allowance amount for this budget cycle.
     *
     * @return the allowance amount
     */
    public double getAllowance() {
        return allowance;
    }

    /**
     * Gets the start date of this budget cycle.
     *
     * @return the start date in YYYY-MM-DD format
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date of this budget cycle.
     *
     * @return the end date in YYYY-MM-DD format
     */
    public String getEndDate() {
        return endDate;
    }
    
    /**
     * Gets the safe daily spending limit for this budget cycle.
     * <p>
     * This is calculated as the remaining balance divided by remaining days.
     * </p>
     *
     * @return the daily limit amount
     */
    public double getDailyLimit() {
        return dailyLimit;
    }
}
