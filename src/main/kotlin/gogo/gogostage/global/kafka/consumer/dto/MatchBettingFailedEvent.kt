package gogo.gogostage.global.kafka.consumer.dto

data class MatchBettingFailedEvent(
    val id: String,
    val studentId: Long,
    val bettingId: Long,
    val bettingPoint: Long,
)
