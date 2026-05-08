package com.masroofy.service;

/**
 * Handles all user notifications in the Masroofy system.
 * Provides methods to display normal messages, confirmation messages,
 * and error messages.
 */
public class NotificationService {

    /**
     * Displays a normal notification message.
     *
     * @param message the message to be displayed
     * @return formatted notification string
     */
    public String showNotification(String message) {
        System.out.println("Notification: " + message);
        return "Notification: " + message;
    }

    /**
     * Displays a confirmation message.
     *
     * @param message the confirmation message
     * @return formatted confirmation string
     */
    public String showConfirmationMessage(String message) {
        System.out.println("Confirmation: " + message);
        return "Confirmation: " + message;
    }

    /**
     * Displays an error message.
     *
     * @param message the error message
     * @return formatted error string
     */
    public String showError(String message) {
        System.err.println("Error: " + message);
        return "Error: " + message;
    }
}
