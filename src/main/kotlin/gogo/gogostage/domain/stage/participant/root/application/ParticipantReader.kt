package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipant
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ParticipantReader(
    private val participantRepository: StageParticipantRepository
) {

    fun read(stageId: Long, studentId: Long): StageParticipant =
        participantRepository.findByStageIdAndStudentId(stageId, studentId)
            ?: throw StageException("Not Found Stage Participant stageId = $stageId, studentid = $stageId", HttpStatus.NOT_FOUND.value())

}
