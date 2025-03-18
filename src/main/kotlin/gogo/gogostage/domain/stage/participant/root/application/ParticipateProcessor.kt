package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.match.root.persistence.MatchRepository
import gogo.gogostage.domain.stage.participant.root.event.TicketPointMinusEvent
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.kafka.consumer.dto.MiniGameBetCompletedEvent
import gogo.gogostage.global.kafka.consumer.dto.TicketShopBuyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Component
class ParticipateProcessor(
    private val matchRepository: MatchRepository,
    private val stageParticipantRepository: StageParticipantRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun matchBetting(matchId: Long, studentId: Long, predictedWinTeamId: Long, point: Long) {
        val match = matchRepository.findNotEndMatchById(matchId)
            ?: throw StageException("Match Not Found, Match id = $matchId", HttpStatus.NOT_FOUND.value())
        validMatchBettingTime(match)
        validMatchBetting(match, studentId)

        val stage = match.game.stage
        val stageParticipant = stageParticipantRepository.queryStageParticipantByStageIdAndStudentId(stage.id, studentId)
                ?: throw StageException("Stage Participant Not Found, Stage id = ${stage.id}, Student id = $studentId", HttpStatus.NOT_FOUND.value())

        stageParticipant.minusPoint(point)
        updateMatchBettingPointTeam(match, predictedWinTeamId, point)

        stageParticipantRepository.save(stageParticipant)
        matchRepository.save(match)
    }

    @Transactional
    fun buyTicket(event: TicketShopBuyEvent) {
        val stageParticipant =
            stageParticipantRepository.queryStageParticipantByStageIdAndStudentId(event.stageId, event.studentId)
                ?: throw StageException("Stage Participant Not Found, Stage id = ${event.stageId}, Student id = ${event.studentId}", HttpStatus.NOT_FOUND.value())

        val totalPrice = event.ticketPrice * event.purchaseQuantity.toLong()

        stageParticipant.minusPoint(totalPrice)
        stageParticipantRepository.save(stageParticipant)

        applicationEventPublisher.publishEvent(
            TicketPointMinusEvent(
                id = UUID.randomUUID().toString(),
                stageId = event.stageId,
                studentId = event.studentId,
                shopMiniGameId = event.shopMiniGameId,
                ticketType = event.ticketType,
                ticketPrice = event.ticketPrice,
                purchaseQuantity = event.purchaseQuantity,
            )
        )
    }

    @Transactional
    fun minigameBetting(event: MiniGameBetCompletedEvent) {
        val stageParticipant =
            stageParticipantRepository.queryStageParticipantByStageIdAndStudentId(event.stageId, event.studentId)
                ?: throw StageException("Stage Participant Not Found, Stage id = ${event.stageId}, Student id = ${event.studentId}", HttpStatus.NOT_FOUND.value())

        if (event.isWin) {
            stageParticipant.plusPoint(event.earnedPoint)
        } else {
            stageParticipant.minusPoint(event.lostedPoint)
        }
        stageParticipantRepository.save(stageParticipant)
    }

    private fun updateMatchBettingPointTeam(
        match: Match,
        predictedWinTeamId: Long,
        point: Long
    ) {
        if (match.aTeam!!.id == predictedWinTeamId) {
            match.addATeamBettingPoint(point)
        } else {
            match.addBTeamBettingPoint(point)
        }
    }

    // 배팅 가능 시간: 매치 시작 24시간 전 ~ 매치 시작 5분 전
    private fun validMatchBettingTime(match: Match) {
        val now = LocalDateTime.now()
        val startDate = match.startDate

        val bettingStartTime = startDate.minusHours(24)
        val bettingEndTime = startDate.minusMinutes(5)

        if (now.isBefore(bettingStartTime) || now.isAfter(bettingEndTime)) {
            throw StageException("Not Betting Able Time", HttpStatus.BAD_REQUEST.value())
        }
    }

    // 본인이 참여한 매치에는 배팅할 수 없음
    private fun validMatchBetting(match: Match, studentId: Long) {
        val aTeam = match.aTeam
        val bTeam = match.bTeam

        val isATeamParticipant = aTeam!!.participants.any { participant -> participant.studentId == studentId }
        val isBTeamParticipant = bTeam!!.participants.any { participant -> participant.studentId == studentId }

        if (isATeamParticipant || isBTeamParticipant) {
            throw StageException("Can't Betting Match Player", HttpStatus.BAD_REQUEST.value())
        }
    }

}
