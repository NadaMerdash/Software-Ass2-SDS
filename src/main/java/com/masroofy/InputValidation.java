import java.util.Date;

public class InputValidation {
    NotificationService notificationService= new NotificationService();

    public void submit(double Allowance, Date startDate, Date endDate) {
        if (validate(Allowance, startDate, endDate)) {
            notificationService.showNotification("Input is valid. Budget Cycle created successfully.");
        }
        else {
            notificationService.showError("Input is invalid. Please try again.");
        }
    }

    public boolean validate(double Allowance, Date startDate, Date endDate) {
        if (Allowance <= 0) {
            notificationService.showError("Allowance must be a positive number.");
            return false;
        }
        if (startDate == null || endDate == null) {
            notificationService.showError("Start date and end date cannot be null.");
            return false;
        }
        return true;
    }

    public boolean validateExpense(double amount, int category) {
        if (amount <= 0) {
            notificationService.showError("Expense amount must be a positive number.");
            return false;
        }
        if (category <= 0) {
            notificationService.showError("Invalid expense category.");
            return false;
        }
        return true;
    }

    public boolean ValidateReset(int userID, int PIN, int SortedPIN) {
        if (userID <= 0) {
            notificationService.showError("Invalid user ID.");
            return false;
        }
        if (PIN <1000 || PIN > 9999) {
            notificationService.showError("Invalid PIN.");
            return false;
        }
        if(PIN != SortedPIN)
        {
            notificationService.showError("Incorrect PIN.");
            return false;
        }
        return true;
    }
}