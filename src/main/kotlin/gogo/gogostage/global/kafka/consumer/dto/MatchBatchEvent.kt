package gogo.gogostage.global.kafka.consumer.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class MatchBatchEvent(
    val id: String,
    val batchId: Long,
    val matchId: Long,
    val victoryTeamId: Long,
    @JsonProperty("ateamScore") val aTeamScore: Int,
    @JsonProperty("bteamScore") val bTeamScore: Int,
    val students: List<StudentBettingDto>,
)

data class StudentBettingDto(
    val studentId: Long,
    val earnedPoint: Long
)
