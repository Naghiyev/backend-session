package az.training.taskmanagement.service;

import az.training.taskmanagement.model.Task;

public interface NotificationService {

    void sendNotification(String taskMessage);

}