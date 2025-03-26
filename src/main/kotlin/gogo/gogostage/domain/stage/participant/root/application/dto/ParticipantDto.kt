package gogo.gogostage.domain.stage.participant.root.application.dto

import java.time.LocalDateTime

data class PointDto(
    val point: Long
)

data class MyTempPointDto(
    val tempPoints: List<TempPointDto>
)

data class TempPointDto(
    val tempPointId: Long,
    val tempPoint: Long,
    val expiredDate: LocalDateTime
)

data class IsParticipantDto(
    val isParticipant: Boolean
)

data class MyPointDto(
    val point: Long
)
