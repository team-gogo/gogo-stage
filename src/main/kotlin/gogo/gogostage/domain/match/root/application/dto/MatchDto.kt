package gogo.gogostage.domain.match.root.application.dto

import com.fasterxml.jackson.annotation.JsonInclude
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

data class MatchToggleDto(
    val isNotice: Boolean
)

data class MatchInfoDto(
    val matchId: Long = 0L,
    val aTeam: MatchTeamInfoDto = MatchTeamInfoDto(),
    val bTeam: MatchTeamInfoDto = MatchTeamInfoDto(),
    val startDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime = LocalDateTime.now(),
    val isEnd: Boolean = false,
    val round: Round? = null,
    val category: GameCategory = GameCategory.ETC,
    val system: GameSystem = GameSystem.SINGLE,
    val gameName: String = "",
    val turn: Int? = null
)

data class MatchSearchDto(
    val count: Int,
    val matches: List<MatchSearchInfoDto>
)

data class MatchSearchInfoDto(
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
    val isNotice: Boolean,
    val isPlayer: Boolean,
    val betting: MatchBettingInfoDto?,
    val result: MatchResultInfoDto?
)

data class MatchTeamInfoDto(
    val teamId: Long? = null,
    val teamName: String = "",
    val bettingPoint: Long? = null,
    val winCount: Int? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val participants: List<MatchTeamParticipantInfoDto>? = null
)

data class MatchTeamParticipantInfoDto(
    val studentId: Long,
    val name: String,
    val classNumber: Int,
    val studentNumber: Int,
    val positionX: String?,
    val positionY: String?,
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
    val earnedPoint: Long?,
    val tempPointExpiredDate: LocalDateTime,
)
