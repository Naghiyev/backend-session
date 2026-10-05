package az.training.taskmanagement.service;

import az.training.taskmanagement.model.Task;

public interface NotificationService {
    void notifyTaskCreated(Task task);
    void notifyTaskUpdated(Task task);
}
