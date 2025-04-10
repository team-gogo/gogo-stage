package gogo.gogostage.domain.game.application

import gogo.gogostage.domain.game.application.dto.*
import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.match.root.persistence.Round
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

    fun mapFormat(matches: List<Match>): QueryGameFormatDto {
        val rounds = listOf(Round.ROUND_OF_32, Round.ROUND_OF_32, Round.QUARTER_FINALS, Round.SEMI_FINALS, Round.FINALS)
        val format = rounds.mapNotNull { makeFormatMatch(matches, it) }
        return QueryGameFormatDto(format)
    }

    fun makeFormatMatch(matches: List<Match>, round: Round): QueryGameFormatInfoDto? {
        val formatMatches = matches
            .filter { it.round == round }
            .map {
                QueryGameFormatMatchInfoDto(
                    matchId = it.id,
                    turn = it.turn!!,
                    aTeamId = it.aTeam?.id,
                    aTeamName = it.aTeam?.name ?: "TBD",
                    bTeamId = it.bTeam?.id,
                    bTeamName = it.bTeam?.name ?: "TBD",
                    isEnd = it.isEnd,
                    winTeamId = it.matchResult?.victoryTeam?.id
                )
            }

        if (formatMatches.isEmpty()) return null

        return QueryGameFormatInfoDto(
            round = round,
            match = formatMatches,
        )
    }

}
