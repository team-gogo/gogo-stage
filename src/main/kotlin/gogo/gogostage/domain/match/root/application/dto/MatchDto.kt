package gogo.gogostage.domain.match.root.application.dto

import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.domain.match.root.persistence.Round
import java.time.LocalDateTime

data class MatchApiInfoDto(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val stage: StageApiInfoDto
)

data class StageApiInfoDto(
    val maintainers: List<Long>
)

data class MatchSearchDto(
    val count: Int,
    val matches: List<MatchInfoDto>
)

data class MatchInfoDto(
    val matchId: Long,
    val aTeam: MatchTeamInfoDto,
    val bTeam: MatchTeamInfoDto,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val isEnd: Boolean,
    val round: Round?,
    val category: GameCategory,
    val gameName: String,
    val system: GameSystem,
    val turn: Int?,
    val betting: MatchBettingInfoDto?,
    val result: MatchResultInfoDto?
)

data class MatchTeamInfoDto(
    val teamId: Long?,
    val teamName: String,
    val bettingPoint: Long?,
    val winCount: Int?,
)

data class MatchBettingInfoDto(
    val isBetting: Boolean?,
    val bettingPoint: Long?,
    val predictedWinTeamId: Long?,
)

data class MatchResultInfoDto(
    val victoryTeamId: Long,
    val aTeamScore: Int,
    val bTeamScore: Int,
    val isPredictionSuccess: Boolean?,
    val earnedPoint: Long?
)
