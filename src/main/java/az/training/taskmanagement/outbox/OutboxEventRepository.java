package az.training.taskmanagement.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    /**
     * Göndəriləcək növbəti partiyanı kilidləyir.
     *
     * FOR UPDATE SKIP LOCKED: app-in bir neçə instance-ı eyni anda işləsə də,
     * bir instance-ın kilidlədiyi sətirləri digəri götürmür, sadəcə ötürür.
     * Beləliklə eyni event iki relay tərəfindən paralel göndərilmir.
     */
    @Query(value = """
            SELECT * FROM outbox_event
             WHERE status = 'PENDING'
               AND next_attempt_at <= now()
             ORDER BY created_at
             LIMIT :limit
             FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<OutboxEvent> lockNextBatch(@Param("limit") int limit);

    List<OutboxEvent> findTop50ByStatusOrderByCreatedAtDesc(OutboxStatus status);

    List<OutboxEvent> findTop50ByOrderByCreatedAtDesc();

    long countByStatus(OutboxStatus status);

    @Modifying
    @Query("DELETE FROM OutboxEvent e WHERE e.status = :status AND e.publishedAt < :before")
    int deleteByStatusAndPublishedAtBefore(@Param("status") OutboxStatus status, @Param("before") Instant before);
}
