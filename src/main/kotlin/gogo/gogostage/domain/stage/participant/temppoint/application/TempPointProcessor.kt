package gogo.gogostage.domain.stage.participant.temppoint.application

import gogo.gogostage.domain.match.result.persistence.MatchResultRepository
import gogo.gogostage.domain.match.root.persistence.MatchRepository
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.domain.stage.participant.temppoint.persistence.TempPoint
import gogo.gogostage.domain.stage.participant.temppoint.persistence.TempPointRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.kafka.consumer.dto.BatchCancelEvent
import gogo.gogostage.global.kafka.consumer.dto.MatchBatchEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class TempPointProcessor(
    private val matchRepository: MatchRepository,
    private val stageParticipantRepository: StageParticipantRepository,
    private val tempPointRepository: TempPointRepository,
    private val matchResultRepository: MatchResultRepository,
) {

    fun addTempPoint(event: MatchBatchEvent) {
        val match = (matchRepository.findNotEndMatchById(event.matchId)
            ?: throw StageException("Match Not Found, Match Id = ${event.matchId}", HttpStatus.NOT_FOUND.value()))
        val stage = match.game.stage

        // * 정산 취소시 동시성 문제 발생을 방지하기 위해 임시 포인트를 5분 5초 후 만료되도록 설정
        val expiredDate = LocalDateTime.now().plusMinutes(5).plusSeconds(5)
        val predictionTempPoints = event.students.map { student ->
            val stageParticipant =
                stageParticipantRepository.queryStageParticipantByStageIdAndStudentId(stage.id, student.studentId)
                    ?: throw StageException(
                        "Stage Participant Not Found, Stage Id = ${stage.id}, Student Id = ${student.studentId}",
                        HttpStatus.NOT_FOUND.value()
                    )

            TempPoint.of(
                stageParticipant = stageParticipant,
                match = match,
                batchId = event.batchId,
                tempPoint = student.earnedPoint,
                tempPointExpiredDate = expiredDate
            )
        }
        tempPointRepository.saveAll(predictionTempPoints)
    }

    fun deleteTempPoint(event: BatchCancelEvent) {
        val now = LocalDateTime.now()
        val tempPoints = tempPointRepository.findNotAppliedByBatchId(now, event.batchId)

        if (tempPoints.isEmpty()) {
            throw StageException("임시 포인트가 이미 반영되었습니다.", HttpStatus.NOT_FOUND.value())
        }

        tempPointRepository.deleteAll(tempPoints)
    }

}
