package gogo.gogostage.global.kafka.consumer.dto

data class MatchBatchEvent(
    val id: String,
    val batchId: Long,
    val matchId: Long,
    val victoryTeamId: Long,
    val aTeamScore: Int,
    val bTeamScore: Int,
    val students: List<StudentBettingDto>
)

data class StudentBettingDto(
    val studentId: Long,
    val earnedPoint: Long
)
