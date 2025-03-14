package gogo.gogostage.domain.stage.root.application.listener

import gogo.gogostage.domain.stage.root.event.CreateStageFastEvent
import gogo.gogostage.domain.stage.root.event.CreateStageOfficialEvent
import gogo.gogostage.domain.stage.root.event.StageConfirmEvent
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
            stagePublisher.publishCreateStageFastEvent(event)
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun createStageOfficial(event: CreateStageOfficialEvent) {
        with(event) {
            log.info("published create stage official application event: {}", id)
            stagePublisher.publishCreateStageOfficialEvent(event)
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun createStageOfficial(event: StageConfirmEvent) {
        with(event) {
            log.info("published stage confirm application event: {}", id)
            stagePublisher.publishStageConfirmEvent(event)
        }
    }

}
