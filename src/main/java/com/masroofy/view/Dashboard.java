package com.masroofy.view;
import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    private JLabel limitLabel;
    private JTextArea transactionsArea;

    public Dashboard() {
        
        setTitle("Dashboard - Financial Overview");
        setSize(400, 300);
        setLayout(new FlowLayout());

        limitLabel = new JLabel("Daily Limit: --");
        transactionsArea = new JTextArea(10, 30);
        
        add(limitLabel);
        add(new JScrollPane(transactionsArea));
    }

    
    public void displayDailyLimit(double limit) {
        this.limitLabel.setText("Daily Limit: $" + limit);
    }

    public void displayTransactions(String transactionsData) {
        this.transactionsArea.setText(transactionsData);
    }

    public void openDashboard() {
        this.setVisible(true);
    }

}