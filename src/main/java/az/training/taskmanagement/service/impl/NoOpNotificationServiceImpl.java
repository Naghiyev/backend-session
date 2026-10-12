package az.training.taskmanagement.service.impl;

import az.training.taskmanagement.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NoOpNotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NoOpNotificationServiceImpl.class);

    @Override
    public void sendNotification(String message) {
        log.info("Sistem xəbərdarlığı (No-Op): {}", message);
    }
}