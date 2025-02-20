package gogo.gogostage.global.kafka.consumer.dto

data class MatchBettingEvent(
    val id: String,
    val studentId: Long,
    val bettingId: Long,
    val matchId: Long,
    val predictedWinTeamId: Long,
    val bettingPoint: Long,
)
