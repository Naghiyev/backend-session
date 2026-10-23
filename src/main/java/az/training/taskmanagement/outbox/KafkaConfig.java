package az.training.taskmanagement.outbox;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    public static final String TASK_EVENTS_TOPIC = "task.events";

    /** App start olanda topic yoxdursa, KafkaAdmin onu yaradır. Key = taskId → 3 partition arasında paylanır. */
    @Bean
    public NewTopic taskEventsTopic() {
        return TopicBuilder.name(TASK_EVENTS_TOPIC).partitions(3).replicas(1).build();
    }

    /**
     * Consumer xətası olanda: 1 saniyə fasilə ilə 3 dəfə yenidən cəhd, sonra log edib keçir.
     * Real layihədə burada DeadLetterPublishingRecoverer ilə DLT topic-ə göndərilir.
     */
    @Bean
    public CommonErrorHandler kafkaErrorHandler() {
        return new DefaultErrorHandler(
                (record, ex) -> log.error("Event emal olunmadı, ötürülür: topic={}, key={}, xəta={}",
                        record.topic(), record.key(), ex.getMessage()),
                new FixedBackOff(1000L, 3));
    }
}
