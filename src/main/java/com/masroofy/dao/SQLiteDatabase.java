package dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// DAO : Handles all DB operations
public class SQLiteDatabase {

    private static SQLiteDatabase instance;
    private Connection conn;

    private SQLiteDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:budget.db");
            if (conn != null) {
            System.out.println(" Connected to SQLite!");
            createTables();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SQLiteDatabase getInstance() {
        if (instance == null) {
            instance = new SQLiteDatabase();
        }
        return instance;
    }
    // ===================== Create tables  =====================
     private void createTables() {
    try (Statement stmt = conn.createStatement()) {
       
        stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                     "id INTEGER PRIMARY KEY, " +
                     "amount REAL, " +
                     "category_id INTEGER, " +
                     "date TEXT);");

        
        stmt.execute("CREATE TABLE IF NOT EXISTS budget_cycle (" +
                     "id INTEGER PRIMARY KEY, " +
                     "allowance REAL, " +
                     "start_date TEXT, " +
                     "end_date TEXT, " +
                     "daily_limit REAL);");

       
        stmt.execute("CREATE TABLE IF NOT EXISTS category (" +
                     "id INTEGER PRIMARY KEY, " +
                     "name TEXT);");
                     
        System.out.println(" All 3 tables are verified and ready!");
    } catch (SQLException e) {
        System.err.println(" Error creating tables: " + e.getMessage());
    }
}

    // ===================== Transactions =====================

    //  Log Expense -> saveTransaction
    public int saveTransaction(double amount, int categoryId, String date) {
        String sql = "INSERT INTO transactions(amount, category_id, date) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, amount);
            stmt.setInt(2, categoryId);
            stmt.setString(3, date);
            stmt.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //  Edit Transaction -> updateTransaction
    public void updateTransaction(int id, double amount, int categoryId) {
        String sql = "UPDATE transactions SET amount=?, category_id=? WHERE id=?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, amount);
            stmt.setInt(2, categoryId);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  Delete Transaction -> deleteTransaction
    public void deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id=?";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  fetchTransaction
    public ResultSet fetchTransaction(int id) {
        String sql = "SELECT * FROM transactions WHERE id=" + id;
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===================== BUDGET =====================

    //  Create Budget Cycle → save
    public void saveBudget(double allowance, String start, String end, double dailyLimit) {
        String sql = "INSERT INTO budget_cycle(allowance, start_date, end_date, daily_limit) VALUES (?,?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, allowance);
            stmt.setString(2, start);
            stmt.setString(3, end);
            stmt.setDouble(4, dailyLimit);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //  getRemainingBalance
    public double getRemainingBalance(double allowance) {
        double spent = getTotalExpenses();
        return allowance - spent;
    }

    public double getTotalExpenses() {
        String sql = "SELECT SUM(amount) FROM transactions";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    //  getRemainingDays
    public int getRemainingDays(String endDate) {
        LocalDate today = LocalDate.now();
        LocalDate end = LocalDate.parse(endDate);

        return (int) ChronoUnit.DAYS.between(today, end);
    }

    // saveSafeDailyLimit
    public void saveSafeDailyLimit(double limit) {
        System.out.println("Safe Daily Limit Saved: " + limit);
    }
}