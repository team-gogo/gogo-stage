package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.team.root.application.dto.GameTeamResDto
import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto
import gogo.gogostage.domain.team.root.application.dto.TeamInfoDto

interface TeamService {
    fun apply(gameId: Long, dto: TeamApplyDto)
    fun getGameTeam(gameId: Long): GameTeamResDto
    fun getGameTempTeam(gameId: Long): GameTeamResDto
    fun getTeamInfo(teamId: Long): TeamInfoDto
}
