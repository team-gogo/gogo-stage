package gogo.gogostage.domain.match.root.application.dto

import java.time.LocalDateTime

data class MatchApiInfoDto(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val stage: StageApiInfoDto
)

data class StageApiInfoDto(
    val maintainers: List<Long>
)
