package gogo.gogostage.domain.game.application.dto

import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.domain.match.root.persistence.Round

data class QueryGameDto(
    val count: Int,
    val games: List<QueryGameInfoDto>
)

data class QueryGameInfoDto(
    val gameId: Long,
    val gameName: String,
    val teamCount: Int,
    val teamMinCapacity: Int,
    val teamMaxCapacity: Int,
    val category: GameCategory,
    val system: GameSystem,
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
