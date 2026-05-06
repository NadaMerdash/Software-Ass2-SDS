package com.masroofy.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.masroofy.controller.ExpenseController;
import com.masroofy.service.InputValidation;
import com.masroofy.service.NotificationService;

public class GUI extends JFrame {
    private static final Color BG_WHITE= Color.WHITE;
    private static final Color SURFACE= new Color(248, 249, 252);
    private static final Color ACCENT_GREEN = new Color(16, 185, 129);
    private static final Color ACCENT_RED= new Color(239, 68, 68);
    private static final Color DARK = new Color(30, 41, 59);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);
    private static final Color BORDER_COLOR = new Color(226, 232, 240);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_INPUT= new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BTN  = new Font("Segoe UI", Font.BOLD, 12);
    private JTextField allowanceField;
    private JTextField startDateField;
    private JTextField endDateField;
    private final ExpenseController controller = new ExpenseController();
    private final InputValidation validation = new InputValidation();
    private final NotificationService notification = new NotificationService();

    public GUI() {
        setTitle("Masroofy");
        setSize(460, 610);
        setMinimumSize(new Dimension(380, 520));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_WHITE);
        add(buildHeader(), BorderLayout.NORTH);
        add(buildFormPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setBackground(BG_WHITE);
        JLabel title = new JLabel("Masroofy");
        title.setFont(FONT_TITLE);
        title.setForeground(DARK);
        p.add(title);
        return p;
    }

    private JPanel buildFormPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        allowanceField = styledField();
        startDateField = styledField();
        endDateField = styledField();
        p.add(label("Allowance"));
        p.add(allowanceField);
        p.add(Box.createVerticalStrut(8));
        p.add(label("Start Date"));
        p.add(startDateField);
        p.add(Box.createVerticalStrut(8));
        p.add(label("End Date"));
        p.add(endDateField);
        return p;
    }
    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(FONT_LABEL);
        l.setForeground(DARK);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(200, 26));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        f.setFont(FONT_INPUT);
        f. setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return f;
    }

    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new GridLayout(2, 3, 10, 10));
        p.setBackground(BG_WHITE);
        JButton save = btn("Save Budget", ACCENT_GREEN);
        JButton add = btn("Add Expense", ACCENT_GREEN);
        JButton edit = btn("Edit", DARK);
        JButton del = btn("Delete", ACCENT_RED);
        JButton reset = btn("Reset", ACCENT_RED);
        JButton dash = btn("Dashboard", DARK);
        save.addActionListener(e -> enterData(
        parseDouble(allowanceField.getText()), new Date(), new Date()));
        add.addActionListener(e -> openAddExpenseDialog());
        edit.addActionListener(e -> editTransaction());
        del.addActionListener(e -> deleteTransaction());
        reset.addActionListener(e -> ResetClick());
        dash.addActionListener(e -> OpenDashboard());
        p.add(save);
        p.add(add);
        p.add(edit);
        p.add(del);
        p.add(reset);
        p.add(dash);
        return p;
    }
    private JButton btn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }
    public void openAddExpenseDialog() {
        JDialog dlg = new JDialog(this, "Add Expense", true);
        dlg.setSize(380, 280);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(true);
        dlg.setLayout(new BorderLayout());
        JPanel form = new JPanel();
        form.setBackground(BG_WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        JTextField amount = styledField();
        JTextField category = styledField();
        form.add(label("Amount"));
        form.add(amount);
        form.add(Box.createVerticalStrut(10));
        form.add(label("Category"));
        form.add(category);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(BG_WHITE);
        JButton ok = new JButton("Add");
        ok.setBackground(ACCENT_GREEN);
        ok.setForeground(Color.WHITE);
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(e -> {
            try {
                double a = Double.parseDouble(amount.getText());
                int c = Integer.parseInt(category.getText());
                if (!validation.validateExpense(a, c)) {
                    notification.showError("Invalid Input");
                    return;
                }
                controller.addExpense(a, c);
                notification.showNotification("Added");
                dlg.dispose();
            } catch (Exception ex) {
                notification.showError("Invalid Input");
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        actions.add(cancel);
        actions.add(ok);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(actions, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }
    public void ResetClick() {
        try {
            String pin = JOptionPane.showInputDialog("Enter PIN");
            if (pin == null) return;
            int p = Integer.parseInt(pin);
            if (!validation.ValidateReset(1, p, 1234)) {
                notification.showError("Wrong PIN");
                return;
            }
            controller.resetCycle(1);
            notification.showNotification("Reset Done");

        } catch (Exception e) {
            notification.showError("Invalid PIN");
        }
    }
    public void enterData(double allowance, Date startDate, Date endDate) {
        if (!validation.validate(allowance, startDate, endDate)) {
            notification.showError("Invalid Input");
            return;
        }
        controller.createBudget(allowance,
                startDateField.getText(),
                endDateField.getText());
        notification.showNotification("Saved");
    }
public void editTransaction() {
    JDialog dlg = new JDialog(this, "Edit Transaction", true);
    dlg.setSize(380, 320);
    dlg.setLocationRelativeTo(this);
    dlg.setResizable(true);
    dlg.setLayout(new BorderLayout());
    JPanel form = new JPanel();
    form.setBackground(BG_WHITE);
    form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
    form.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
    JTextField id = styledField();
    JTextField amount = styledField();
    JTextField category = styledField();
    form.add(label("Transaction ID"));
    form.add(id);
    form.add(Box.createVerticalStrut(10));
    form.add(label("New Amount"));
    form.add(amount);
    form.add(Box.createVerticalStrut(10));
    form.add(label("New Category"));
    form.add(category);
    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    actions.setBackground(BG_WHITE);
    JButton ok = new JButton("Save");
    ok.setBackground(DARK);
    ok.setForeground(Color.WHITE);
    JButton cancel = new JButton("Cancel");

    ok.addActionListener(e -> {
        try {
            int i = Integer.parseInt(id.getText());
            double a = Double.parseDouble(amount.getText());
            int c = Integer.parseInt(category.getText());

            if (!validation.validateExpense(a, c)) {
                notification.showError("Invalid Input");
                return;
            }

            controller.editTransaction(i, a, c);
            notification.showNotification("Updated");
            dlg.dispose();

        } catch (Exception ex) {
            notification.showError("Invalid Input");
        }
    });
    cancel.addActionListener(e -> dlg.dispose());
    actions.add(cancel);
    actions.add(ok);
    dlg.add(form, BorderLayout.CENTER);
    dlg.add(actions, BorderLayout.SOUTH);
    dlg.setVisible(true);
}

public void deleteTransaction() {
    JDialog dlg = new JDialog(this, "Delete Transaction", true);
    dlg.setSize(350, 200);
    dlg.setLocationRelativeTo(this);
    dlg.setResizable(true);
    dlg.setLayout(new BorderLayout());
    JPanel form = new JPanel();
    form.setBackground(BG_WHITE);
    form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
    form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
    JTextField id = styledField();
    form.add(label("Transaction ID"));
    form.add(id);
    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    actions.setBackground(BG_WHITE);
    JButton del = new JButton("Delete");
    del.setBackground(ACCENT_RED);
    del.setForeground(Color.WHITE);
    JButton cancel = new JButton("Cancel");

    del.addActionListener(e -> {
        try {
            int i = Integer.parseInt(id.getText());
            controller.deleteTransaction(i);
            notification.showNotification("Deleted");
            dlg.dispose();

        } catch (Exception ex) {
            notification.showError("Invalid ID");
        }
    });

    cancel.addActionListener(e -> dlg.dispose());
    actions.add(cancel);
    actions.add(del);
    dlg.add(form, BorderLayout.CENTER);
    dlg.add(actions, BorderLayout.SOUTH);

    dlg.setVisible(true);
}
    public void OpenDashboard() {
        Dashboard d = new Dashboard();
        d.openDashboard();
        dispose();
    }
    private double parseDouble(String t) {
        try { return Double.parseDouble(t); }
        catch (Exception e) {
            notification.showError("Invalid number");
            return 0;
        }
    }
    public static void main(String[] args) {
        new GUI();
    }
}