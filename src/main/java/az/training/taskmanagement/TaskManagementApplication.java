package az.training.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Spring Boot application-ın giriş nöqtəsi.
 *
 * @SpringBootApplication üç annotasiyanı birləşdirir:
 *  - @Configuration      (bean tərifləri)
 *  - @EnableAutoConfiguration (Spring-in avtomatik konfiqurasiyası)
 *  - @ComponentScan      (bu paketdən aşağı @Component/@Service/@Repository/@RestController axtarışı)
 *
 * Lesson 2-də əl ilə etdiyimiz "wiring"-i indi Spring avtomatik edir (DI).
 */
@SpringBootApplication
@EnableCaching
public class TaskManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApplication.class, args);
    }
}
