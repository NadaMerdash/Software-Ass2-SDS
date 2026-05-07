package com.masroofy.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.masroofy.dao.SQLiteDatabase;
import com.masroofy.service.BudgetManager;
import com.masroofy.service.TransactionManager;

public class Dashboard extends JFrame {

    private JLabel limitLabel;
    private JLabel balanceLabel;
    private JLabel daysLabel;
    private JLabel percentageLabel;
    private JPanel listPanel;

    private int currentUserId;
    private GUI home;

    SQLiteDatabase db = SQLiteDatabase.getInstance();
    BudgetManager budgetManager = new BudgetManager();
    TransactionManager transactionManager = new TransactionManager();

    public Dashboard(int userId, GUI home) {
        this.currentUserId = userId;
        this.home = home;

        setTitle("Dashboard");
        setSize(460, 610);
        setMinimumSize(new Dimension(380, 520));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Dashboard Overview", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        headerLabel.setForeground(new Color(44, 62, 80));
        headerLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statsPanel.setBackground(new Color(240, 240, 240));

        limitLabel = new JLabel("Safe Daily Limit: --");
        balanceLabel = new JLabel("Remaining Balance: --");
        daysLabel = new JLabel("Remaining Days: --");
        percentageLabel = new JLabel("Spent Percentage: --");

        limitLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        balanceLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        daysLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        percentageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        limitLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        balanceLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        daysLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        percentageLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        statsPanel.add(limitLabel);
        statsPanel.add(balanceLabel);
        statsPanel.add(daysLabel);
        statsPanel.add(percentageLabel);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        footerPanel.setBackground(Color.WHITE);

        JButton homeBtn = new JButton("Home Page");
        homeBtn.setBackground(new Color(44, 62, 80));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFocusPainted(false);
        homeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        homeBtn.setPreferredSize(new Dimension(200, 40));

        homeBtn.addActionListener(e -> {
            home.setVisible(true);
            this.dispose();
        });

        footerPanel.add(homeBtn);
        add(footerPanel, BorderLayout.SOUTH);
        loadData();
        setVisible(true);
    }

    private void loadData() {
        double safeLimit = budgetManager.getSafeDailyLimit(currentUserId);
        double balance = db.getRemainingBalance(currentUserId);
        int days = db.getRemainingDays(currentUserId);
        double totalBudget = db.getAllowance(currentUserId);
        String percent = budgetManager.calculatePercentage(currentUserId, totalBudget);

        limitLabel.setText("Safe Daily Limit: " + String.format("%.2f", safeLimit) + " EGP");
        balanceLabel.setText("Remaining Balance: " + String.format("%.2f", balance) + " EGP");
        daysLabel.setText("Remaining Days: " + days + " Days");
        percentageLabel.setText("Spent Percentage: " + percent);

        refreshTransactions();
    }

    public void refreshTransactions() {
        listPanel.removeAll();
        List<String[]> transactions = transactionManager.getAllTransactions(currentUserId);
        for (String[] t : transactions) {
            addTransactionItem(t[3], t[1] + " EGP", t[2]);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }
    private void addTransactionItem(String date, String amount, String category) {
        JPanel item = new JPanel(new BorderLayout());
        item.setMaximumSize(new Dimension(440, 40));
        item.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        item.add(new JLabel(" " + date), BorderLayout.WEST);
        item.add(new JLabel(amount + "   "), BorderLayout.EAST);
        item.add(new JLabel(category, SwingConstants.CENTER), BorderLayout.CENTER);

        listPanel.add(item);
    }

    public void refreshDashboard() {
        loadData();
    }
}