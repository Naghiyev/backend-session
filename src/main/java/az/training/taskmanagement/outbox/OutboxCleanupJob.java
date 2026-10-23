package az.training.taskmanagement.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Göndərilmiş (SENT) eventləri müəyyən müddətdən sonra silir ki, outbox cədvəli sonsuz böyüməsin.
 * PENDING və FAILED sətirlərə toxunmur.
 */
@Component
public class OutboxCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(OutboxCleanupJob.class);

    private final OutboxEventRepository repository;
    private final OutboxProperties props;

    public OutboxCleanupJob(OutboxEventRepository repository, OutboxProperties props) {
        this.repository = repository;
        this.props = props;
    }

    @Scheduled(cron = "${outbox.cleanup-cron:0 15 3 * * *}", zone = "Asia/Baku")
    @Transactional
    public void purgeSentEvents() {
        Instant before = Instant.now().minus(props.retention());
        int deleted = repository.deleteByStatusAndPublishedAtBefore(OutboxStatus.SENT, before);
        log.info("Outbox təmizləndi: {} SENT event silindi ({}-dən köhnə)", deleted, before);
    }
}
