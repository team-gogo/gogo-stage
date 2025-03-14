package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.game.application.GameReader
import gogo.gogostage.domain.team.root.application.dto.GameTeamResDto
import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TeamServiceImpl(
    private val userUtil: UserContextUtil,
    private val gameReader: GameReader,
    private val teamValidator: TeamValidator,
    private val teamProcessor: TeamProcessor,
    private val teamReader: TeamReader,
    private val teamMapper: TeamMapper
) : TeamService {

    @Transactional
    override fun apply(gameId: Long, dto: TeamApplyDto) {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        teamValidator.validApply(student, game, dto)

        teamProcessor.apply(game, dto)
    }

    override fun getGameTeam(gameId: Long): GameTeamResDto {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        teamValidator.validStageParticipant(student.studentId, game.stage.id)
        val teams = teamReader.readParticipatingTeamByGameId(game.id, true)
        return teamMapper.mapGameTeam(teams)
    }

}
