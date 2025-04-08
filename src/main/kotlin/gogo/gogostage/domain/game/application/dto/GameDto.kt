package gogo.gogostage.domain.game.application.dto

import com.fasterxml.jackson.annotation.JsonProperty
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
    val format: List<QueryGameFormatInfoDto> = emptyList(),
)

data class QueryGameFormatInfoDto(
    val round: Round = Round.FINALS,
    val match: List<QueryGameFormatMatchInfoDto> = emptyList(),
)

data class QueryGameFormatMatchInfoDto(
    val matchId: Long = 0L,
    val turn: Int = 0,
    @field:JsonProperty("ateamId")
    val aTeamId: Long? = null,
    @field:JsonProperty("ateamName")
    val aTeamName: String = "",
    @field:JsonProperty("bteamId")
    val bTeamId: Long? = null,
    @field:JsonProperty("bteamName")
    val bTeamName: String = "",
    @field:JsonProperty("end")
    val isEnd: Boolean = false,
    val winTeamId: Long? = null,
)
