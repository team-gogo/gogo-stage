package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto

interface TeamService {
    fun apply(gameId: Long, dto: TeamApplyDto)
}
