package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.service.NotificationService;

public class NoOpNotificationService implements NotificationService {
    @Override
    public void onTaskCreated(Task task) {

    }

    @Override
    public void onTaskUpdated(Task task) {

    }
}
