package gogo.gogostage.global.kafka.consumer.dto

data class MiniGameBetCompletedEvent(
    val id: String,
    val earnedPoint: Long,
    val lostedPoint: Long,
    val isWin: Boolean,
    val studentId: Long,
    val stageId: Long,
    val gameType: MiniGameType
)

enum class MiniGameType {
    YAVARWEE, PLINKO, COINTOSS
}
