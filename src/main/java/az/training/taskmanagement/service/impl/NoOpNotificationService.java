package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;
import az.training.taskmanagement.service.NotificationService;

public class NoOpNotificationService implements NotificationService {
    @Override
    public void notificationTaskCreate(Task task) {

    }

    @Override
    public void notificationStatusChange(Task task, TaskStatus oldTaskStatus) {

    }
}
