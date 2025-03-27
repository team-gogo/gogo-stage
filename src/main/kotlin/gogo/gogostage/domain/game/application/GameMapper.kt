package gogo.gogostage.domain.game.application

import gogo.gogostage.domain.game.application.dto.QueryGameDto
import gogo.gogostage.domain.game.application.dto.QueryGameInfoDto
import gogo.gogostage.domain.game.persistence.Game
import org.springframework.stereotype.Component

@Component
class GameMapper {

    fun mapAll(games: List<Game>): QueryGameDto = QueryGameDto(
        count = games.size,
        games = games.map {
            QueryGameInfoDto(
                gameId = it.id,
                gameName = it.name,
                teamCount = it.teamCount,
                teamMinCapacity = it.teamMinCapacity,
                teamMaxCapacity = it.teamMaxCapacity,
                category = it.category,
                system = it.system
            )
        }
    )

}
