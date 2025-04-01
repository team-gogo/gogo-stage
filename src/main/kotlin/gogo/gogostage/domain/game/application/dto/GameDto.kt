package gogo.gogostage.domain.game.application.dto

import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.game.persistence.GameSystem

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
