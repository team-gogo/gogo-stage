package gogo.gogostage.global.kafka.consumer.dto

data class MiniGameBetCompletedFailedEvent(
    val id: String,
    val studentId: Long,
    val stageId: Long,
    val gameType: MiniGameType
)
