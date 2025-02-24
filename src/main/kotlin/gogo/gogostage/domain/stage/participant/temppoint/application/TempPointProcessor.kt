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

        val victoryTeam = teamRepository.findByIdOrNull(event.victoryTeamId)
            ?: throw StageException(
                "Victory Team Not Found, Team Id = ${event.victoryTeamId}",
                HttpStatus.NOT_FOUND.value()
            )

        val matchResult = MatchResult.of(
            match = match,
            victoryTeam = victoryTeam,
            aTeamScore = event.aTeamScore,
            bTeamScore = event.bTeamScore,
        )
        matchResultRepository.save(matchResult)

        val expiredDate = LocalDateTime.now().plusMinutes(5)
        val tempPoints = event.students.map { student ->
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
        tempPointRepository.saveAll(tempPoints)
    }

}
