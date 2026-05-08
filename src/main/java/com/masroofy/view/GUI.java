package com.masroofy.view;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.masroofy.controller.ExpenseController;

/**
 * Main GUI window for the Masroofy budget management application.
 * <p>
 * Provides a comprehensive interface for user authentication, budget management,
 * and expense tracking. Uses CardLayout to switch between login and main screens.
 * Includes dialogs for adding, editing, and deleting transactions, plus budget
 * creation and cycle reset functionality.
 * </p>
 *
 * @author Nada
 * @version 1.0
 */
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
    private static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 12);
    private static final Color ACCENT_BLUE  = new Color(59, 130, 246);
    private static final Font FONT_SUB = new Font("Segoe UI", Font.PLAIN, 12);

    private JTextField loginUserIdField;
    private JPasswordField loginPinField;
    private JLabel loginStatusLabel;
    private JTextField allowanceField;
    private JTextField startDateField;
    private JTextField endDateField;
    private final ExpenseController controller = new ExpenseController();

    private static final String[] CATEGORY_NAMES = {
        "1 - Food", "2 - Transport", "3 - Entertainment",
        "4 - Shopping", "5 - Health", "6 - Education", "7 - Other"
    };

    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JLabel welcomeLabel;

    /**
     * Constructs the main GUI window.
     * <p>
     * Initializes the window with CardLayout for switching between login and main screens.
     * Starts with the login panel displayed.
     * </p>
     */
    public GUI() {
        setTitle("Masroofy");
        setSize(460, 610);
        setMinimumSize(new Dimension(380, 520));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(buildMainPanel(),  "MAIN");
        mainContainer.add(buildLoginPanel(), "LOGIN");
        add(mainContainer);
        cardLayout.show(mainContainer, "LOGIN"); 
        setVisible(true);
    }

    /**
     * Builds the login/registration panel.
     * <p>
     * Creates a styled card with User ID and PIN input fields,
     * plus a login/register button with validation feedback.
     * </p>
     *
     * @return the configured login panel
     */
    private JPanel buildLoginPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(SURFACE);
        JPanel card = new JPanel();
        card.setBackground(BG_WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(28, 32, 28, 32)
        ));

        JLabel title = new JLabel("Masroofy", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = new JLabel("Sign in or create a new account", SwingConstants.CENTER);
        sub.setFont(FONT_SUB);
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginUserIdField = styledField();
        loginPinField    = new JPasswordField();
        applyFieldStyle(loginPinField);
        loginStatusLabel = new JLabel(" ");
        loginStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        loginStatusLabel.setForeground(TEXT_MUTED);
        loginStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = new JButton("Login / Register");
        loginBtn.setBackground(ACCENT_BLUE);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(FONT_BTN);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        loginBtn.addActionListener(e -> handleLogin());
        loginPinField.addActionListener(e -> handleLogin());

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(20));
        card.add(makeFieldBlock("User ID", loginUserIdField));
        card.add(Box.createVerticalStrut(12));
        card.add(makeFieldBlock("PIN (4 digits)", loginPinField));
        card.add(Box.createVerticalStrut(6));
        card.add(loginStatusLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(loginBtn);

        outer.add(card);
        return outer;
    }

    /**
     * Handles login/registration button action.
     * <p>
     * Validates input format, attempts authentication/registration,
     * and displays appropriate error messages or switches to main panel.
     * </p>
     */
    private void handleLogin() {
        String userIdText = loginUserIdField.getText().trim();
        String pinText    = new String(loginPinField.getPassword()).trim();

        if (userIdText.isEmpty() || pinText.isEmpty()) {
            setLoginError("Please fill in all fields.");
            return;
        }

        int userId, pin;
        try {
            userId = Integer.parseInt(userIdText);
            pin    = Integer.parseInt(pinText);
        } catch (NumberFormatException ex) {
            setLoginError("User ID and PIN must be numbers.");
            return;
        }

        if (pin < 1000 || pin > 9999) {
            setLoginError("PIN must be exactly 4 digits.");
            return;
        }
        boolean isNew = controller.isNewUser(userId);
        boolean ok    = controller.loginOrRegister(userId, pin);
        if (ok) {
            welcomeLabel.setText("Welcome, User" + userId +
                (isNew ? "  (New account created)" : " "));
            cardLayout.show(mainContainer, "MAIN");
        } else {
            setLoginError("Wrong PIN. Please try again.");
            loginPinField.setText("");
        }
    }

    /**
     * Sets and displays a login error message.
     *
     * @param msg the error message to display
     */
    private void setLoginError(String msg) {
        loginStatusLabel.setText("  " + msg);
        loginStatusLabel.setForeground(ACCENT_RED);
    }

    /**
     * Builds the application header panel.
     * <p>
     * Creates the title and welcome message display area.
     * </p>
     *
     * @return the configured header panel
     */
    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setBackground(BG_WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(12, 20, 6, 20));
        JLabel title = new JLabel("Masroofy", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel = new JLabel(" ", SwingConstants.CENTER);
        welcomeLabel.setFont(FONT_SUB);
        welcomeLabel.setForeground(TEXT_MUTED);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(3));
        p.add(welcomeLabel);
        return p;
    }

    /**
     * Builds the main application panel.
     * <p>
     * Assembles header, form fields, and action buttons into the main interface.
     * </p>
     *
     * @return the configured main panel
     */
    private JPanel buildMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_WHITE);
        panel.add(buildHeader(), BorderLayout.NORTH);
        panel.add(buildFormPanel(),BorderLayout.CENTER);
        panel.add(buildButtonPanel(),BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds the form panel with budget input fields.
     * <p>
     * Creates input fields for allowance, start date, and end date.
     * </p>
     *
     * @return the configured form panel
     */
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

    /**
     * Builds the action buttons panel.
     * <p>
     * Creates six buttons for budget operations: Save, Add Expense, Edit, Delete, Reset, Dashboard.
     * </p>
     *
     * @return the configured button panel
     */
    private JPanel buildButtonPanel() {
        JPanel p = new JPanel(new GridLayout(2, 3, 10, 10));
        p.setBackground(BG_WHITE);
        p.setBorder(new EmptyBorder(8, 16, 16, 16));

        JButton save  = btn("Save Budget",ACCENT_GREEN);
        JButton add   = btn("Add Expense",ACCENT_GREEN);
        JButton edit  = btn("Edit", DARK);
        JButton del   = btn("Delete", ACCENT_RED);
        JButton reset = btn("Reset",ACCENT_RED);
        JButton dash  = btn("Dashboard",DARK);
        save .addActionListener(e -> saveBudget());
        add  .addActionListener(e -> openAddExpenseDialog());
        edit .addActionListener(e -> editTransaction());
        del  .addActionListener(e -> deleteTransaction());
        reset.addActionListener(e -> ResetClick());
        dash .addActionListener(e -> openDashboard(controller.getCurrentUserId()));
        p.add(save); p.add(add);  p.add(edit);
        p.add(del);  p.add(reset); p.add(dash);
        return p;
    }

    /**
     * Creates a styled label for form fields.
     *
     * @param t the label text
     * @return the configured JLabel
     */
    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(FONT_LABEL);
        l.setForeground(DARK);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    /**
     * Creates a styled text input field.
     *
     * @return the configured JTextField
     */
    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(200, 26));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        f.setFont(FONT_INPUT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return f;
    }

    /**
     * Applies styling to a text input field.
     *
     * @param f the field to style
     */
    private void applyFieldStyle(JTextField f) {
        f.setPreferredSize(new Dimension(200, 28));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        f.setFont(FONT_INPUT);
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        f.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER_COLOR),
        BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    /**
     * Creates a styled combo box with category options.
     *
     * @return the configured JComboBox
     */
    private JComboBox<String> styledCombo() {
        JComboBox<String> combo = new JComboBox<>(CATEGORY_NAMES);
        applyComboStyle(combo);
        return combo;
    }

    /**
     * Applies styling to a combo box.
     *
     * @param combo the combo box to style
     */
    private void applyComboStyle(JComboBox<?> combo) {
        combo.setPreferredSize(new Dimension(200, 28));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        combo.setFont(FONT_INPUT);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setBackground(BG_WHITE);
    }

    /**
     * Creates a labeled form field block.
     *
     * @param labelText the field label
     * @param field the input component
     * @return the configured field block panel
     */
    private JPanel makeFieldBlock(String labelText, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_WHITE);
        p.setOpaque(true); 
        JLabel l = new JLabel(labelText);
        l.setFont(FONT_LABEL);
        l.setForeground(DARK);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(3));
        p.add(field);
        return p;
    }

    /**
     * Creates a dialog window with specified dimensions.
     *
     * @param title the dialog title
     * @param w the dialog width
     * @param h the dialog height
     * @return the configured JDialog
     */
    private JDialog makeDialog(String title, int w, int h) {
        JDialog dlg = new JDialog(this, title, true);
        dlg.setSize(w, h);
        dlg.setLocationRelativeTo(this);
        dlg.setResizable(true);
        dlg.setLayout(new BorderLayout());
        return dlg;
    }

    /**
     * Creates a form panel for dialogs.
     *
     * @return the configured form panel
     */
    private JPanel dialogForm() {
        JPanel p = new JPanel();
        p.setBackground(BG_WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(16, 20, 10, 20));
        return p;
    }

    /**
     * Completes dialog setup by adding buttons and showing it.
     *
     * @param dlg the dialog to finalize
     * @param form the form panel
     * @param cancel the cancel button
     * @param action the action button
     */
    private void finishDialog(JDialog dlg, JPanel form, JButton cancel, JButton action) {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actions.setBackground(BG_WHITE);
        actions.add(cancel);
        actions.add(action);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(actions, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    /**
     * Creates a styled button with specified text and color.
     *
     * @param t the button text
     * @param c the button background color
     * @return the configured JButton
     */
    private JButton btn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    /**
     * Opens a dialog for adding a new expense.
     * <p>
     * Prompts for expense amount and category, validates input, and adds to database.
     * </p>
     */
    public void openAddExpenseDialog() {
        JDialog dlg = makeDialog("Add Expense", 400, 255);
        JPanel form = dialogForm();
        JTextField amountField = styledField();
        JComboBox<String> catCombo = styledCombo(); 
        form.add(makeFieldBlock("Amount", amountField));
        form.add(Box.createVerticalStrut(10));
        form.add(makeFieldBlock("Category", catCombo));

        JButton ok = btn("Add",    ACCENT_GREEN);
        JButton cancel = btn("Cancel", new Color(148, 163, 184));

        ok.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                int catId     = catCombo.getSelectedIndex() + 1;
                if (!controller.validateExpense(amount, catId)) {
                    JOptionPane.showMessageDialog(
                        dlg, controller.showError("Invalid input."), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                controller.addExpense(amount, catId);
                JOptionPane.showMessageDialog(
                    dlg, controller.showNotification("Expense added!"), "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
            } catch (NumberFormatException ex) { 
                JOptionPane.showMessageDialog(dlg, controller.showError("Amount must be a number."), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });
        cancel.addActionListener(e -> dlg.dispose());

        finishDialog(dlg, form, cancel, ok);
    }

    /**
     * Opens a dialog for editing an existing transaction.
     * <p>
     * Displays list of transactions, allows selecting amount and category to update.
     * </p>
     */
    public void editTransaction() {
        List<String[]> txns = controller.getAllTransactions();
        if (txns.isEmpty()) {
            JOptionPane.showMessageDialog(this, controller.showNotification("No transactions found."), "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JDialog dlg  = makeDialog("Edit Transaction", 420, 315);
        JPanel form  = dialogForm();
        JComboBox<String> txnCombo = new JComboBox<>();
        applyComboStyle(txnCombo);
        for (String[] t : txns)
            txnCombo.addItem("ID:" + t[0] + "  |  " + t[1] + "   |  " + t[2] + "  |  " + t[3]);
        JTextField amountField = styledField();
        JComboBox<String> catCombo = styledCombo();
        form.add(makeFieldBlock("Select Transaction", txnCombo));
        form.add(Box.createVerticalStrut(10));
        form.add(makeFieldBlock("New Amount", amountField));
        form.add(Box.createVerticalStrut(10));
        form.add(makeFieldBlock("New Category", catCombo));
        JButton ok = btn("Save",   DARK);
        JButton cancel = btn("Cancel", new Color(148, 163, 184));
        ok.addActionListener(e -> {
            try {
                int idx   = txnCombo.getSelectedIndex();
                int txnId = Integer.parseInt(txns.get(idx)[0]);
                double amount = Double.parseDouble(amountField.getText().trim());
                int catId     = catCombo.getSelectedIndex() + 1;
                if (!controller.validateExpense(amount, catId)) {
                    JOptionPane.showMessageDialog(
                    dlg, controller.showError("Invalid input."), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                controller.editTransaction(txnId, amount, catId);
                JOptionPane.showMessageDialog(
                    dlg, controller.showNotification("Transaction updated!"), "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
            } catch (NumberFormatException ex) 
            { 
             JOptionPane.showMessageDialog(dlg, controller.showError("Amount must be a number."), "Error", JOptionPane.ERROR_MESSAGE); 
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        finishDialog(dlg, form, cancel, ok);
    }

    /**
     * Opens a dialog for deleting a transaction.
     * <p>
     * Displays list of transactions and prompts for confirmation before deletion.
     * </p>
     */
    public void deleteTransaction() {
        List<String[]> txns = controller.getAllTransactions();
        if (txns.isEmpty()) {
            JOptionPane.showMessageDialog(this, controller.showNotification("No transactions found."), "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JDialog dlg  = makeDialog("Delete Transaction", 420, 210);
        JPanel form  = dialogForm();
        JComboBox<String> txnCombo = new JComboBox<>();
        applyComboStyle(txnCombo);
        for (String[] t : txns)
            txnCombo.addItem("ID:" + t[0] + "  |  " + t[1] + "   |  " + t[2] + "  |  " + t[3]);
        form.add(makeFieldBlock("Select Transaction to Delete", txnCombo));
        JButton del = btn("Delete", ACCENT_RED);
        JButton cancel = btn("Cancel", new Color(148, 163, 184));
        del.addActionListener(e -> {
            int idx   = txnCombo.getSelectedIndex();
            int txnId = Integer.parseInt(txns.get(idx)[0]);
            int confirm = JOptionPane.showConfirmDialog(dlg,
                "Delete transaction ID " + txnId + "?\nThis cannot be undone.",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteTransaction(txnId);
                JOptionPane.showMessageDialog(dlg, controller.showNotification("Deleted."), "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
            }
        });
        cancel.addActionListener(e -> dlg.dispose());
        finishDialog(dlg, form, cancel, del);
    }

    /**
     * Handles the reset budget cycle action.
     * <p>
     * Prompts for PIN confirmation and deletes all transactions for the current cycle.
     * </p>
     */
    public void ResetClick() {
        String pinInput = JOptionPane.showInputDialog(this,
          controller.showNotification("Enter your PIN to confirm reset:"), "Reset", JOptionPane.WARNING_MESSAGE);
        if (pinInput == null) return;
        int pin;
        try { 
            pin = Integer.parseInt(pinInput.trim()); 
        }
        catch (NumberFormatException e) { 
            JOptionPane.showMessageDialog(this, controller.showError("PIN must be a number."),
             "Error",JOptionPane.ERROR_MESSAGE);
            return; 
        }
        int storedPin = controller.getStoredPin();
        if (!controller.validateReset(controller.getCurrentUserId(), pin, storedPin)) {
            JOptionPane.showMessageDialog(this, controller.showError("Incorrect PIN. Reset cancelled."), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            controller.showConfirmationMessage("All your transactions will be deleted. Continue?"),
            "Confirm Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.resetCycle();
            JOptionPane.showMessageDialog(this, controller.showNotification("Reset complete. All transactions cleared."),
                "Done", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Handles budget save action.
     * <p>
     * Validates budget parameters (allowance and date range), creates budget cycle,
     * and displays confirmation message.
     * </p>
     */
    private void saveBudget() {
        String allowTxt = allowanceField.getText().trim();
        String startTxt = startDateField.getText().trim();
        String endTxt   = endDateField.getText().trim(); 
        double allowance;
        Date startDate, endDate;
        try {
            allowance = Double.parseDouble(allowTxt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                controller.showError("Allowance must be a number."),
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            startDate = java.sql.Date.valueOf(startTxt);
            endDate   = java.sql.Date.valueOf(endTxt);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                controller.showError("Dates must be in YYYY-MM-DD format."),
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!controller.validate(allowance, startDate, endDate)) {
            JOptionPane.showMessageDialog(this, controller.showError("Invalid budget details."), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        controller.createBudget(allowance, startTxt, endTxt);
        JOptionPane.showMessageDialog(this, controller.showNotification("Budget saved!"),
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Opens the Dashboard window for the current user.
     *
     * @param userId the user ID to display dashboard for
     */
    public void openDashboard(int userId) {
        Dashboard dash = new Dashboard(userId, this, controller);
        dash.setVisible(true);
        this.setVisible(false);
    }

    /**
     * Parses a string to a double value with error handling.
     *
     * @param t the string to parse
     * @return the parsed double value, or 0 if parsing fails
     */
    private double parseDouble(String t) {
        try { 
            return Double.parseDouble(t); 
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, controller.showError("Invalid number"), "Error", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }
}
