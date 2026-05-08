package com.masroofy.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.masroofy.model.Budget;
import com.masroofy.model.Transaction;

/**
 * Data Access Object (DAO) for SQLite database operations.
 * <p>
 * This class handles all database interactions for the Masroofy application,
 * including user management, transaction tracking, and budget cycle management.
 * Implements the Singleton pattern to ensure a single database connection.
 * </p>
 *
 * @author Nada
 * @version 1.0
 */
public class SQLiteDatabase {

    private static SQLiteDatabase instance;
    private Connection conn;

    /**
     * Private constructor for the Singleton pattern.
     * <p>
     * Initializes the SQLite database connection and sets up required tables.
     * </p>
     */
    private SQLiteDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:budget.db");
            if (conn != null) {
                System.out.println(" Connected to SQLite!");
                createTables();
                migrateSchema();
                seedCategories();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the singleton instance of SQLiteDatabase.
     * <p>
     * Creates the instance if it does not exist.
     * </p>
     *
     * @return the singleton instance of SQLiteDatabase
     */
    public static SQLiteDatabase getInstance() {
        if (instance == null) {
            instance = new SQLiteDatabase();
        }
        return instance;
    }

    /**
     * Creates all required database tables if they do not exist.
     * <p>
     * Creates tables for users, transactions, budget cycles, and categories.
     * </p>
     */
    private void createTables() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY,
                    pin INTEGER NOT NULL,
                    stored_pin INTEGER NOT NULL
                );
            """);
            
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    amount REAL,
                    category_id INTEGER,
                    date TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS budget_cycle (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER,
                    allowance REAL,
                    start_date TEXT,
                    end_date TEXT,
                    daily_limit REAL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS category (
                    id INTEGER PRIMARY KEY,
                    name TEXT
                );
            """);
            System.out.println("All tables ready!");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Migrates the database schema by adding missing columns.
     * <p>
     * Adds the user_id column to transactions and budget_cycle tables if not present.
     * </p>
     */
    private void migrateSchema() {
        try (Statement stmt = conn.createStatement()) {
            try { 
                stmt.execute("ALTER TABLE transactions ADD COLUMN user_id INTEGER DEFAULT 1"); 
            }
            catch (SQLException ignored) {}
            try { 
                stmt.execute("ALTER TABLE budget_cycle ADD COLUMN user_id INTEGER DEFAULT 1"); 
            }
            catch (SQLException ignored) {} 
        } catch (SQLException e) {
            System.err.println("Migration error: " + e.getMessage());
        }
    }

    /**
     * Seeds the category table with default expense categories.
     * <p>
     * Inserts categories: Food, Transport, Entertainment, Shopping, Health, Education, Other.
     * </p>
     */
    private void seedCategories() {
        String[] cats = {"Food", "Transport", "Entertainment",
                         "Shopping", "Health", "Education", "Other"};
        String sql = "INSERT OR IGNORE INTO category(id, name) VALUES (?,?)";
        try {
            for (int i = 0; i < cats.length; i++) {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, i + 1);
                ps.setString(2, cats[i]);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a new user to the database.
     *
     * @param userId the unique user ID
     * @param pin the user's PIN
     * @return true if the user was saved successfully, false otherwise
     */
    public boolean saveUser(int userId, int pin) {
        int storedPin = pin;
        String sql =
            "INSERT OR REPLACE INTO users(id, pin, stored_pin) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, pin);
            ps.setInt(3, storedPin);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a user exists in the database.
     *
     * @param userId the user ID to check
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(int userId) {
        String sql = "SELECT id FROM users WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) { 
                return rs.next(); 
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return false;
    }

    /**
     * Validates a user's PIN.
     *
     * @param userId the user ID
     * @param pin the PIN to validate
     * @return true if the PIN is correct, false otherwise
     */
    public boolean validateUser(int userId, int pin) {
        String sql = "SELECT pin FROM users WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("pin") == pin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves the stored PIN for a user.
     *
     * @param userId the user ID
     * @return the stored PIN, or -1 if not found
     */
    public int getStoredPin(int userId) {
        String sql = "SELECT stored_pin FROM users WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("stored_pin");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
    

    /**
     * Saves a new transaction to the database.
     *
     * @param transaction the transaction to save
     * @return 1 if successful, 0 if an error occurred
     */
    public int saveTransaction(Transaction transaction) {
        String sql =
            "INSERT INTO transactions(user_id, amount, category_id, date) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, transaction.getUserId());
            ps.setDouble(2, transaction.getAmount());
            ps.setInt(3, transaction.getCategoryId());
            ps.setString(4, transaction.getDate());
            
            ps.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Updates an existing transaction in the database.
     *
     * @param transaction the transaction with updated information
     */
    public void updateTransaction(Transaction transaction) {
        String sql =
            "UPDATE transactions SET amount=?, category_id=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, transaction.getAmount());
            ps.setInt(2, transaction.getCategoryId());
            ps.setInt(3, transaction.getTransactionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a transaction from the database.
     *
     * @param id the transaction ID to delete
     */
    public void deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the allowance for a user's current budget.
     *
     * @param userId the user ID
     * @return the allowance amount, or 0 if not found
     */
    public double getAllowance(int userId) {
        String sql = "SELECT allowance FROM budget_cycle WHERE user_id = ?"; 
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("allowance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; 
    }

    /**
     * Deletes all transactions for a user.
     *
     * @param userID the user ID
     * @return the number of transactions deleted
     */
    public int deleteAllTransactions(int userID) {
        String sql = "DELETE FROM transactions WHERE user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Retrieves all transactions for a user.
     * <p>
     * Returns transactions in descending order (newest first) with category names joined from the category table.
     * </p>
     *
     * @param userId the user ID
     * @return a list of string arrays containing [ID, Amount, Category Name, Date]
     */
    public List<String[]> getAllTransactions(int userId) {
        List<String[]> list = new ArrayList<>();
        String sql =
            "SELECT t.id, t.amount, COALESCE(c.name,'Unknown') AS cat_name, t.date " +
            "FROM transactions t LEFT JOIN category c ON t.category_id = c.id " +
            "WHERE t.user_id = ? ORDER BY t.id DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        String.format("%.2f", rs.getDouble("amount")),
                        rs.getString("cat_name"),
                        rs.getString("date")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Fetches a specific transaction from the database.
     *
     * @param id the transaction ID
     * @return a ResultSet containing the transaction data, or null if not found
     */
    public ResultSet fetchTransaction(int id) {
        String sql = "SELECT * FROM transactions WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Saves a new budget cycle to the database.
     *
     * @param budget the budget to save
     */
    public void saveBudget(Budget budget) {
        String sql =
            "INSERT INTO budget_cycle(user_id,allowance,start_date,end_date,daily_limit) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, budget.getUserID());
            ps.setDouble(2, budget.getAllowance());
            ps.setString(3, budget.getStartDate());
            ps.setString(4, budget.getEndDate());
            ps.setDouble(5, budget.getDailyLimit());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the remaining balance in the current budget.
     * <p>
     * Subtracts total expenses from the allowance amount.
     * </p>
     *
     * @param userId the user ID
     * @return the remaining balance
     */
    public double getRemainingBalance(int userId) {
        double spent = getTotalExpenses(userId);
        double allowance = 0.0;
        String sql =
            "SELECT allowance FROM budget_cycle WHERE user_id=? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    allowance = rs.getDouble("allowance");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allowance - spent;
    }

    /**
     * Retrieves the total expenses for a user across all transactions.
     *
     * @param userId the user ID
     * @return the sum of all transaction amounts, or 0 if no transactions exist
     */
    public double getTotalExpenses(int userId) {
        String sql =
            "SELECT SUM(amount) FROM transactions WHERE user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Retrieves the current safe daily spending limit for a user.
     *
     * @param userId the user ID
     * @return the daily limit, or 0 if no budget exists
     */
    public double getSafeDailyLimit(int userId) {
        String sql = "SELECT daily_limit FROM budget_cycle WHERE user_id=? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("daily_limit");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Calculates the remaining days in the current budget cycle.
     * <p>
     * Determines the number of days from today until the budget end date.
     * </p>
     *
     * @param userId the user ID
     * @return the number of remaining days, or 0 if the budget has ended or no budget exists
     */
    public int getRemainingDays(int userId) {
        String sql =
            "SELECT start_date, end_date FROM budget_cycle WHERE user_id=? ORDER BY id DESC LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate start = LocalDate.parse(rs.getString("start_date"));
                    LocalDate end = LocalDate.parse(rs.getString("end_date"));
                    LocalDate now = LocalDate.now();
                    LocalDate from = now.isAfter(start) ? now : start;
                    long days = ChronoUnit.DAYS.between(from, end);
                    return (days < 0) ? 0 : (int) days;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Updates the safe daily limit for a user's budget.
     *
     * @param limit the new daily limit
     * @param userId the user ID
     */
    public void saveSafeDailyLimit(double limit, int userId) {
        String sql = "UPDATE budget_cycle SET daily_limit=? WHERE user_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, limit);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
