package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.service.NotificationService;

public class NoOpNotificationService implements NotificationService {

    @Override
    public void sendNotification(Task task) {
        
    }
}