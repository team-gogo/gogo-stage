package gogo.gogostage.domain.game.application.dto

import com.fasterxml.jackson.annotation.JsonProperty
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.domain.match.root.persistence.Round

data class QueryGameDto(
    val count: Int,
    val games: List<QueryGameInfoDto>,
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
    val match: List<QueryGameFormatMatchInfoDto>
)

data class QueryGameFormatMatchInfoDto(
    val matchId: Long,
    val turn: Int,
    @field:JsonProperty("ateamId")
    val aTeamId: Long?,
    @field:JsonProperty("ateamName")
    val aTeamName: String,
    @field:JsonProperty("bteamId")
    val bTeamId: Long?,
    @field:JsonProperty("bteamName")
    val bTeamName: String,
    @field:JsonProperty("end")
    val isEnd: Boolean,
    val winTeamId: Long?
)
