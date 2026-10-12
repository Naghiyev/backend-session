package az.training.taskmanagement.service;

import az.training.taskmanagement.model.Task;
import az.training.taskmanagement.model.TaskStatus;

public interface NotificationService {
    void notificationTaskCreate(Task task);
    void notificationStatusChange(Task task , TaskStatus oldTaskStatus);
}
