package gogo.gogostage.domain.stage.participant.temppoint.application

import gogo.gogostage.domain.match.result.persistence.MatchResult
import gogo.gogostage.domain.match.result.persistence.MatchResultRepository
import gogo.gogostage.domain.match.root.persistence.MatchRepository
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.domain.stage.participant.temppoint.persistence.TempPoint
import gogo.gogostage.domain.stage.participant.temppoint.persistence.TempPointRepository
import gogo.gogostage.domain.team.root.persistence.TeamRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.kafka.consumer.dto.MatchBatchEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.math.roundToLong

@Component
class TempPointProcessor(
    private val matchRepository: MatchRepository,
    private val stageParticipantRepository: StageParticipantRepository,
    private val tempPointRepository: TempPointRepository,
    private val teamRepository: TeamRepository,
    private val matchResultRepository: MatchResultRepository,
) {

    @Transactional
    fun addTempPoint(event: MatchBatchEvent) {
        val match = (matchRepository.findNotEndMatchById(event.matchId)
            ?: throw StageException("Match Not Found, Match Id = ${event.matchId}", HttpStatus.NOT_FOUND.value()))
        val stage = match.game.stage
        val totalBettingPoint = match.aTeamBettingPoint + match.bTeamBettingPoint

        val victoryTeam = teamRepository.findByIdOrNull(event.victoryTeamId)
            ?: throw StageException(
                "Victory Team Not Found, Team Id = ${event.victoryTeamId}",
                HttpStatus.NOT_FOUND.value()
            )

        match.end()
        matchRepository.save(match)

        val matchResult = MatchResult.of(
            match = match,
            victoryTeam = victoryTeam,
            aTeamScore = event.aTeamScore,
            bTeamScore = event.bTeamScore,
        )
        matchResultRepository.save(matchResult)

        // * 정산 취소시 동시성 문제 발생을 방지하기 위해 5분 5초 후 만료되도록 설정
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

        // * 해당 매치의 승리 팀 선수들은 해당 매치에 배팅된 포인트의 10%씩 임시포인트 지급
        val victoryTempPoint = victoryTeam.participants.map { participant ->
            val stageParticipant =
                stageParticipantRepository.queryStageParticipantByStageIdAndStudentId(stage.id, participant.studentId)
                    ?: throw StageException(
                        "Stage Participant Not Found, Stage Id = ${stage.id}, Student Id = ${participant.studentId}",
                        HttpStatus.NOT_FOUND.value()
                    )

            TempPoint.of(
                stageParticipant = stageParticipant,
                match = match,
                batchId = event.batchId,
                tempPoint = (totalBettingPoint * 0.1).roundToLong(),
                tempPointExpiredDate = expiredDate
            )
        }
        tempPointRepository.saveAll(victoryTempPoint)
    }

}
