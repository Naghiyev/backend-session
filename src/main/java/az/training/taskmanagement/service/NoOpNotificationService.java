package az.training.taskmanagement.service;

import az.training.taskmanagement.model.Task;

public class NoOpNotificationService implements NotificationService{
    @Override
    public void notifyTaskCreated(Task task) {

    }
}
