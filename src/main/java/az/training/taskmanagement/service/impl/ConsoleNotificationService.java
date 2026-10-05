package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.service.NotificationService;

/**
 * Console logging implementation of NotificationService.
 */
public class ConsoleNotificationService implements NotificationService {

    @Override
    public void sendNotification(String message) {
        System.out.println("[NOTIFICATION]: " + message);
    }
}
