package az.training.taskmanagement.service;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;

public class NoOpNotificationService implements NotificationService {

    @Override
    public void sendNotification(String taskMessage) {

    }
}
