package gogo.gogostage.domain.game.application.dto

import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.domain.match.root.persistence.Round

data class QueryGameDto(
    val count: Int = 0,
    val games: List<QueryGameInfoDto> = emptyList(),
)

data class QueryGameInfoDto(
    val gameId: Long = 0L,
    val gameName: String = "",
    val teamCount: Int = 0,
    val teamMinCapacity: Int = 0,
    val teamMaxCapacity: Int = 0,
    val category: GameCategory = GameCategory.ETC,
    val system: GameSystem = GameSystem.SINGLE,
)

data class QueryGameFormatDto(
    val format: List<QueryGameFormatInfoDto>
)

data class QueryGameFormatInfoDto(
    val round: Round,
    val match: List<QueryGameFormatMatchInfoDto>,
)

data class QueryGameFormatMatchInfoDto(
    val matchId: Long,
    val turn: Int,
    val aTeamId: Long?,
    val aTeamName: String,
    val bTeamId: Long?,
    val bTeamName: String,
    val isEnd: Boolean,
    val winTeamId: Long?
)
