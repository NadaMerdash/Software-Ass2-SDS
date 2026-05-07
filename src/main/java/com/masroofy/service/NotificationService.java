package com.masroofy.service;

public class NotificationService {
    public String showNotification(String message) {
        System.out.println("Notification: " + message);
        return "Notification: " + message;
    }

    public String showConfirmationMessage(String message) {
        System.out.println("Confirmation: " + message);
        return "Confirmation: " + message;
    }

    public String showError(String message) {
        System.err.println("Error: " + message);
        return "Error: " + message;
    }
}