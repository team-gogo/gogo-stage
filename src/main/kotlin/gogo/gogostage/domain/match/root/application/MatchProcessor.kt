package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.domain.match.result.persistence.MatchResult
import gogo.gogostage.domain.match.result.persistence.MatchResultRepository
import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.match.root.persistence.MatchRepository
import gogo.gogostage.domain.match.root.persistence.Round
import gogo.gogostage.domain.stage.participant.temppoint.application.TempPointProcessor
import gogo.gogostage.domain.team.root.persistence.Team
import gogo.gogostage.domain.team.root.persistence.TeamRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.kafka.consumer.dto.MatchBatchEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MatchProcessor(
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository,
    private val matchResultRepository: MatchResultRepository,
    private val tempPointProcessor: TempPointProcessor
) {

    @Transactional
    fun batch(event: MatchBatchEvent) {
        val match = matchRepository.findNotEndMatchById(event.matchId)
            ?: throw StageException("Match Not Found, Match Id = ${event.matchId}", HttpStatus.NOT_FOUND.value())
        val victoryTeam = teamRepository.findByIdOrNull(event.victoryTeamId)
            ?: throw StageException("Victory Team Not Found, Team Id = ${event.victoryTeamId}", HttpStatus.NOT_FOUND.value())

        victoryTeam.addWinCount()
        teamRepository.save(victoryTeam)

        if (match.game.system == GameSystem.TOURNAMENT) {
            when (match.round!!) {
                Round.ROUND_OF_32 -> {
                    updateNextMatch(match, victoryTeam, Round.ROUND_OF_16)
                }
                Round.ROUND_OF_16 -> {
                    updateNextMatch(match, victoryTeam, Round.QUARTER_FINALS)
                }
                Round.QUARTER_FINALS -> {
                    updateNextMatch(match, victoryTeam, Round.SEMI_FINALS)
                }
                Round.SEMI_FINALS -> {
                    updateNextMatch(match, victoryTeam, Round.FINALS)
                }
                Round.FINALS -> return
            }
        }

        match.end()
        matchRepository.save(match)

        val matchResult = MatchResult.of(
            match = match,
            victoryTeam = victoryTeam,
            aTeamScore = event.aTeamScore,
            bTeamScore = event.bTeamScore,
        )
        matchResultRepository.save(matchResult)

        tempPointProcessor.addTempPoint(event)
    }

    private fun updateNextMatch(
        match: Match,
        victoryTeam: Team,
        nextRound: Round
    ) {
        val nextTurn = if (match.turn!! % 2 == 0) match.turn!! / 2 else match.turn!! / 2 + 1
        val nextTeamPosition = if (match.turn!! % 2 == 0) 'B' else 'A'

        val nextMatch = matchRepository.findByGameIdAndRoundAndTurn(match.game.id, nextRound, nextTurn)
            ?: throw StageException("Not Found Match, Match Id = ${match.id}", HttpStatus.NOT_FOUND.value())

        when (nextTeamPosition) {
            'A' -> {
                nextMatch.updateATeam(victoryTeam)
            }

            'B' -> {
                nextMatch.updateBTeam(victoryTeam)
            }
        }
    }

}
