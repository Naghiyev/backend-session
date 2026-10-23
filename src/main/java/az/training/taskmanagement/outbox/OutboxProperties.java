package az.training.taskmanagement.outbox;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

/**
 * application.properties-dəki outbox.* parametrləri.
 *
 * @param batchSize    bir dövrdə maksimum neçə event götürülür
 * @param maxAttempts  neçə uğursuz cəhddən sonra event FAILED olur
 * @param sendTimeout  Kafka cavabını nə qədər gözləyirik
 * @param retention    SENT eventlər neçə gün saxlanılır (sonra silinir)
 */
@ConfigurationProperties(prefix = "outbox")
public record OutboxProperties(
        @DefaultValue("100") int batchSize,
        @DefaultValue("10") int maxAttempts,
        @DefaultValue("5s") Duration sendTimeout,
        @DefaultValue("7d") Duration retention) {
}
