package gogo.gogostage.domain.stage.participant.root.application.listener

import gogo.gogostage.domain.stage.participant.root.event.TicketPointMinusEvent
import gogo.gogostage.global.kafka.publisher.StagePublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class StageParticipantApplicationEventListener(
    private val stagePublisher: StagePublisher
) {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun createStageFast(event: TicketPointMinusEvent) {
        with(event) {
            log.info("published ticket point minus application event: {}", id)
            stagePublisher.publishTicketPointMinusEvent(event)
        }
    }

}
