package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.team.root.persistence.Team
import gogo.gogostage.domain.team.root.persistence.TeamRepository
import gogo.gogostage.global.error.StageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class TeamReader(
    private val teamRepository: TeamRepository
) {

    fun readParticipatingTeamByGameId(gameId: Long, isParticipating: Boolean) =
        teamRepository.findAllByGameIdAndIsParticipating(gameId, isParticipating)

    fun read(teamId: Long) =
        teamRepository.findByIdOrNull(teamId)
            ?: throw StageException("Team Not Found, teamId = $teamId", HttpStatus.NOT_FOUND.value())

    fun readTeamInfo(team: Team) =
        teamRepository.queryTeamInfo(team)
}