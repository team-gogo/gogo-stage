package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.notification.persistence.MatchNotification
import gogo.gogostage.domain.match.notification.persistence.MatchNotificationRepository
import gogo.gogostage.domain.match.root.application.dto.MatchToggleDto
import gogo.gogostage.domain.game.persistence.GameRepository
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
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import gogo.gogostage.global.kafka.consumer.dto.BatchCancelEvent
import gogo.gogostage.global.kafka.consumer.dto.MatchBatchEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MatchProcessor(
    private val matchNotificationRepository: MatchNotificationRepository,
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository,
    private val matchResultRepository: MatchResultRepository,
    private val tempPointProcessor: TempPointProcessor,
    private val gameRepository: GameRepository
) {

    fun toggleMatchNotification(match: Match, student: StudentByIdStub): MatchToggleDto {
        if (matchNotificationRepository.existsByMatchIdAndStudentId(match.id, student.studentId)) {
            val matchNotification = matchNotificationRepository.findByMatchIdAndStudentId(match.id, student.studentId)
                ?: throw StageException("MatchNotification Not Found, matchId=${match.id}, studentId=${student.studentId}", HttpStatus.NOT_FOUND.value())

            matchNotificationRepository.delete(matchNotification)

            return MatchToggleDto(
                isNotice = false
            )
        } else {
            val matchNotification = MatchNotification(
                match = match,
                studentId = student.studentId
            )

            matchNotificationRepository.save(matchNotification)

            return MatchToggleDto(
                isNotice = true
            )
        }
    }

    @Transactional
    fun batch(event: MatchBatchEvent) {
        val match = matchRepository.findNotEndMatchById(event.matchId)
            ?: throw StageException("Match Not Found, Match Id = ${event.matchId}", HttpStatus.NOT_FOUND.value())

        val victoryTeamId = event.victoryTeamId
        val victoryTeam = teamRepository.findByIdOrNull(victoryTeamId)
            ?: throw StageException("Victory Team Not Found, Team Id = ${victoryTeamId}", HttpStatus.NOT_FOUND.value())

        victoryTeam.addWinCount()
        teamRepository.save(victoryTeam)

        when (match.game.system) {
            GameSystem.SINGLE -> {
                gameEnd(match, victoryTeam)
            }

            GameSystem.FULL_LEAGUE -> {
                if (match.leagueTurn == match.game.leagueCount) {
                    val finalWinTeam = teamRepository.findTop1Teams(match.game.id)
                    gameEnd(match, finalWinTeam)
                }
            }

            GameSystem.TOURNAMENT -> {

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

                    Round.FINALS -> {
                        gameEnd(match, victoryTeam)
                    }
                }

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

    @Transactional
    fun cancelBatch(event: BatchCancelEvent) {
        val match = (matchRepository.findByIdOrNull(event.matchId)
            ?: throw StageException("매치를 찾을 수 없습니다. Match Id = ${event.matchId}.", HttpStatus.NOT_FOUND.value()))
        match.batchRollback()
        matchRepository.save(match)

        val game = match.game
        if (game.system == GameSystem.TOURNAMENT && match.round!! != Round.FINALS) {

            when (match.round!!) {
                Round.ROUND_OF_32 -> {
                    updateNextMatch(match, null, Round.ROUND_OF_16)
                }

                Round.ROUND_OF_16 -> {
                    updateNextMatch(match, null, Round.QUARTER_FINALS)
                }

                Round.QUARTER_FINALS -> {
                    updateNextMatch(match, null, Round.SEMI_FINALS)
                }

                Round.SEMI_FINALS -> {
                    updateNextMatch(match, null, Round.FINALS)
                }
                else -> {}
            }

        }

        if (game.isEnd) {
            game.endRollBack()
            gameRepository.save(game)
        }

        matchResultRepository.deleteByMatchId(match.id)

        tempPointProcessor.deleteTempPoint(event)
    }


    private fun gameEnd(
        match: Match,
        victoryTeam: Team?
    ) {
        val game = match.game
        game.end(victoryTeam)
        gameRepository.save(game)
    }

    private fun updateNextMatch(
        match: Match,
        updateTeam: Team?,
        nextRound: Round
    ) {
        val nextTurn = if (match.turn!! % 2 == 0) match.turn!! / 2 else match.turn!! / 2 + 1
        val nextTeamPosition = if (match.turn!! % 2 == 0) 'B' else 'A'

        val nextMatch = matchRepository.findByGameIdAndRoundAndTurn(match.game.id, nextRound, nextTurn)
            ?: throw StageException("Not Found Match, Match Id = ${match.id}", HttpStatus.NOT_FOUND.value())

        when (nextTeamPosition) {
            'A' -> {
                nextMatch.updateATeam(updateTeam)
            }

            'B' -> {
                nextMatch.updateBTeam(updateTeam)
            }
        }
    }

}
