package com.masroofy.view;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Dashboard extends JFrame {

    private final JLabel limitLabel;
    private final JTextArea transactionsArea;

    public Dashboard() {
        
        setTitle("Dashboard - Financial Overview");
        setSize(400, 300);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        limitLabel = new JLabel("Daily Limit: --");
        transactionsArea = new JTextArea(10, 30);
        transactionsArea.setEditable(false);
        
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