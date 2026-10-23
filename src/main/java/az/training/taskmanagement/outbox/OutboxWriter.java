package az.training.taskmanagement.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Eventi outbox cədvəlinə yazır.
 *
 * Propagation.MANDATORY: bu metod YALNIZ artıq açıq olan tranzaksiya daxilində çağırıla bilər.
 * Kimsə onu tranzaksiyasız çağırsa, Spring dərhal IllegalTransactionStateException atır.
 * Bu, "event biznes məlumatından ayrı commit olundu" səhvinin qarşısını kod səviyyəsində alır.
 */
@Component
public class OutboxWriter {

    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxWriter(OutboxEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void append(String aggregateType, String aggregateId, String eventType, Object event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            repository.save(OutboxEvent.pending(aggregateType, aggregateId, eventType, json));
        } catch (JsonProcessingException e) {
            // Exception biznes tranzaksiyasını da rollback edir: event yazılmırsa, task da yazılmasın.
            throw new IllegalStateException("Outbox event serializasiya olunmadı: " + eventType, e);
        }
    }
}
