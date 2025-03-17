package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipant
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.domain.stage.participant.temppoint.persistence.TempPoint
import gogo.gogostage.domain.stage.participant.temppoint.persistence.TempPointRepository
import gogo.gogostage.domain.stage.root.persistence.StageStatus
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ParticipantReader(
    private val participantRepository: StageParticipantRepository,
    private val tempPointRepository: TempPointRepository
) {

    fun read(stageId: Long, studentId: Long): StageParticipant =
        participantRepository.queryStudentPoint(stageId, studentId, StageStatus.CONFIRMED)
            ?: throw StageException("Not Found Stage Participant stageId = $stageId, studentid = $stageId", HttpStatus.NOT_FOUND.value())

    fun readTempPointList(stageParticipantId: Long): List<TempPoint> =
        tempPointRepository.findByStageParticipantId(stageParticipantId)

    fun readStageParticipantByStageIdAndStudentId(stageId: Long, studentId: Long) =
        participantRepository.findByStageIdAndStudentId(stageId, studentId)
            ?: throw StageException("StageParticipant Not Found, stageId = $stageId, studentid = $studentId", HttpStatus.NOT_FOUND.value())
}
