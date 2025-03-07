package gogo.gogostage.domain.stage.root.application.listener

import gogo.gogostage.domain.stage.root.event.CreateStageFastEvent
import gogo.gogostage.global.kafka.publisher.StagePublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class StageApplicationEventListener(
    private val stagePublisher: StagePublisher,
) {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun createStageFast(event: CreateStageFastEvent) {
        with(event) {
            log.info("published create stage fast application event: {}", id)
            stagePublisher.publishCreateStageFastEVent(event)
        }
    }

}
