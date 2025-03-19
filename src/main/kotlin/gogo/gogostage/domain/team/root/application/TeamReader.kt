package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.team.root.persistence.TeamRepository
import org.springframework.stereotype.Component

@Component
class TeamReader(
    private val teamRepository: TeamRepository
) {

    fun readParticipatingTeamByGameId(gameId: Long, isParticipating: Boolean) =
        teamRepository.findAllByGameIdAndIsParticipating(gameId, isParticipating)

}