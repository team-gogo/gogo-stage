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
    @field:JsonProperty("aTeamId")
    val aTeamId: Long?,
    @field:JsonProperty("aTeamName")
    val aTeamName: String,
    @field:JsonProperty("bTeamId")
    val bTeamId: Long?,
    @field:JsonProperty("bTeamName")
    val bTeamName: String,
    val isEnd: Boolean,
    val winTeamId: Long?
)
