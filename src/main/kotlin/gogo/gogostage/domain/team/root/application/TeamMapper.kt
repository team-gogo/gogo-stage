package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.team.root.application.dto.GameTeamDto
import gogo.gogostage.domain.team.root.application.dto.GameTeamResDto
import gogo.gogostage.domain.team.root.persistence.Team
import org.springframework.stereotype.Component

@Component
class TeamMapper {

    fun mapGameTeam(teams: List<Team>): GameTeamResDto {
        val gameTeamListDto = teams.map {GameTeamDto(
                teamId = it.id,
                teamName = it.name,
                participantCount = it.participantCount,
                winCount = it.winCount,
            )}

        return GameTeamResDto(
            count = gameTeamListDto.count(),
            team = gameTeamListDto
        )
    }

}