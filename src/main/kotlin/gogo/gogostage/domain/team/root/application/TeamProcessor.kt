package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.game.persistence.GameRepository
import gogo.gogostage.domain.team.participant.persistence.TeamParticipant
import gogo.gogostage.domain.team.participant.persistence.TeamParticipantRepository
import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto
import gogo.gogostage.domain.team.root.persistence.Team
import gogo.gogostage.domain.team.root.persistence.TeamRepository
import org.springframework.stereotype.Component

@Component
class TeamProcessor(
    private val teamRepository: TeamRepository,
    private val teamParticipantRepository: TeamParticipantRepository,
    private val gameRepository: GameRepository
) {

    fun apply(game: Game, dto: TeamApplyDto) {
        val team = Team.of(game, dto.teamName, dto.participants.size)
        teamRepository.save(team)

        game.addTeam()
        gameRepository.save(game)

        val participants = dto.participants.map {
            TeamParticipant.of(team, it.studentId, it.positionX, it.positionY)
        }
        teamParticipantRepository.saveAll(participants)
    }

}
