public class NotificationService {
    public void showNotification(String message) {
        System.out.println("Notification: " + message);
    }

    public boolean showConfirmationMessage(String message) {
        System.out.println("Confirmation: " + message);
       return true;
    }

    public void showError(String message) {
        System.err.println("Error: " + message);
    }
}