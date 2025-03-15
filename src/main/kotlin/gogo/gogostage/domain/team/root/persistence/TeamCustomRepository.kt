package gogo.gogostage.domain.team.root.persistence

import gogo.gogostage.domain.team.root.application.dto.TeamInfoDto

interface TeamCustomRepository {
    fun queryTeamInfo(team: Team): TeamInfoDto
}