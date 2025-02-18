package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.match.root.persistence.MatchRepository
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ParticipateProcessor(
    private val matchRepository: MatchRepository,
    private val stageParticipantRepository: StageParticipantRepository
) {

    @Transactional
    fun matchBetting(matchId: Long, studentId: Long, predictedWinTeamId: Long, point: Long) {
        val match = matchRepository.findNotEndMatchById(matchId)
            ?: throw StageException("Match Not Found, Match id = $matchId", HttpStatus.NOT_FOUND.value())

        val stage = match.game.stage
        val stageParticipant = stageParticipantRepository.queryStageParticipantByStageIdAndStudentId(stage.id, studentId)
                ?: throw StageException("Stage Participant Not Found, Stage id = ${stage.id}, Student id = $studentId", HttpStatus.NOT_FOUND.value())

        stageParticipant.minusPoint(point)
        updateMatchBettingPointTeam(match, predictedWinTeamId, point)

        stageParticipantRepository.save(stageParticipant)
        matchRepository.save(match)
    }

    private fun updateMatchBettingPointTeam(
        match: Match,
        predictedWinTeamId: Long,
        point: Long
    ) {
        if (match.aTeam.id == predictedWinTeamId) {
            match.addATeamBettingPoint(point)
        } else {
            match.addBTeamBettingPoint(point)
        }
    }

}
