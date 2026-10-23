package az.training.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot application-ın giriş nöqtəsi.
 *
 * @SpringBootApplication üç annotasiyanı birləşdirir:
 *  - @Configuration      (bean tərifləri)
 *  - @EnableAutoConfiguration (Spring-in avtomatik konfiqurasiyası)
 *  - @ComponentScan      (bu paketdən aşağı @Component/@Service/@Repository/@RestController axtarışı)
 *
 * Lesson 2-də əl ilə etdiyimiz "wiring"-i indi Spring avtomatik edir (DI).
 *
 * @EnableScheduling: OutboxRelay və OutboxCleanupJob-un @Scheduled metodlarını işə salır.
 * @ConfigurationPropertiesScan: OutboxProperties (outbox.*) record-unu tapır.
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@ConfigurationPropertiesScan
public class TaskManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApplication.class, args);
    }
}
