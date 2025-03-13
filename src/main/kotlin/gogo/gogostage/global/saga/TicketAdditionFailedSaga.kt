package gogo.gogostage.global.saga

import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.kafka.consumer.dto.TicketAdditionFailedEvent
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TicketAdditionFailedSaga(private val stageParticipantRepository: StageParticipantRepository) {

    @Transactional
    fun rollbackPoint(event: TicketAdditionFailedEvent) {
        val stageParticipant =
            stageParticipantRepository .queryStageParticipantByStageIdAndStudentId(event.stageId, event.studentId)
                ?: throw StageException("Stage Participant Not Found -- SAGA.rollbackPoint(stageId = ${event.stageId}, studentId = ${event.studentId})", HttpStatus.NOT_FOUND.value())

        val totalPrice = event.ticketPrice * event.purchaseQuantity.toLong()
        stageParticipant.plusPoint(totalPrice)
    }

}
