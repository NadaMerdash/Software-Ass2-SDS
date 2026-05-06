package com.masroofy.view;
import java.awt.FlowLayout;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.masroofy.controller.ExpenseController;
import com.masroofy.service.InputValidation;
import com.masroofy.service.NotificationService;

public class GUI extends JFrame {
    private JTextField allowanceField;
    private final JTextField startDateField;
    private final JTextField endDateField;
    private final ExpenseController controller = new ExpenseController();
    private final InputValidation validation = new InputValidation();
    private final NotificationService notification = new NotificationService();

    public GUI() {
        setTitle("Masroofy");
        setSize(450, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        allowanceField = new JTextField(15);
        startDateField = new JTextField(15);
        endDateField = new JTextField(15);
        JButton saveBtn = new JButton("Save Budget");
        JButton resetBtn = new JButton("Reset Cycle");
        JButton dashBtn = new JButton("Open Dashboard");

        saveBtn.addActionListener(e -> enterData(
                parseDouble(allowanceField.getText()),
                new Date(),
                new Date()
        ));

        resetBtn.addActionListener(e -> ResetClick());
        dashBtn.addActionListener(e -> OpenDashboard());

        add(new JLabel("Allowance"));
        add(allowanceField);

        add(new JLabel("Start Date"));
        add(startDateField);

        add(new JLabel("End Date"));
        add(endDateField);

        add(saveBtn);
        add(resetBtn);
        add(dashBtn);

        setVisible(true);
    }

    public void enterData(double allowance, Date startDate, Date endDate) {

        if (!validation.validate(allowance, startDate, endDate)) {
            notification.showError("Invalid Input Data");
            return;
        }

        controller.createBudget(
                allowance,
                startDateField.getText(),
                endDateField.getText()
        );

        notification.showNotification("Budget Saved Successfully");
    }

    public void ResetClick() {

        boolean confirmed = notification.showConfirmationMessage("Are you sure you want to reset cycle?");

        if (confirmed) {
            controller.resetCycle(1);
            notification.showNotification("Cycle Reset Successfully");
        } else {
            notification.showNotification("Reset Cancelled");
        }
    }

    public void OpenDashboard() {
        Dashboard dashboard = new Dashboard();
        dashboard.openDashboard();
        dispose();
    }

    public void showScreen() {
        setVisible(true);
    }

    public void closeDialog() {
        dispose();
    }

    private double parseDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            notification.showError("Invalid number format");
            return 0;
        }
    }

    public static void main(String[] args) {
        new GUI();
    }
}