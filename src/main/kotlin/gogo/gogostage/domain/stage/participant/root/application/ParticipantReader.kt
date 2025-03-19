package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.stage.participant.root.application.dto.IsParticipantDto
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipant
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.domain.stage.root.persistence.StageStatus
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ParticipantReader(
    private val participantRepository: StageParticipantRepository
) {

    fun read(stageId: Long, studentId: Long): StageParticipant =
        participantRepository.queryStudentPoint(stageId, studentId, StageStatus.CONFIRMED)
            ?: throw StageException("Not Found Stage Participant stageId = $stageId, studentid = $stageId", HttpStatus.NOT_FOUND.value())

    fun isParticipant(stageId: Long, studentId: Long): Boolean =
        participantRepository.existsByStageIdAndStudentId(stageId, studentId)

}
