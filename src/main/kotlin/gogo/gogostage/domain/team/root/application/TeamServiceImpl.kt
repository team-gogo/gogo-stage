package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.game.application.GameReader
import gogo.gogostage.domain.team.root.application.dto.GameTeamResDto
import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto
import gogo.gogostage.domain.team.root.application.dto.TeamInfoDto
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
    private val teamMapper: TeamMapper,
) : TeamService {

    @Transactional
    override fun apply(gameId: Long, dto: TeamApplyDto) {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        teamValidator.validApply(student, game, dto)

        teamProcessor.apply(game, dto)
    }

    @Transactional(readOnly = true)
    override fun getGameTeam(gameId: Long): GameTeamResDto {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        teamValidator.validStageParticipant(student.studentId, game.stage.id)
        val teams = teamReader.readParticipatingTeamByGameId(game.id, true)
        return teamMapper.mapGameTeam(teams)
    }

    @Transactional(readOnly = true)
    override fun getGameTempTeam(gameId: Long): GameTeamResDto {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        teamValidator.validStageParticipant(student.studentId, game.stage.id)
        val tempTeams = teamReader.readParticipatingTeamByGameId(game.id, false)
        return teamMapper.mapGameTeam(tempTeams)
    }

    @Transactional(readOnly = true)
    override fun getTeamInfo(teamId: Long): TeamInfoDto {
        val student = userUtil.getCurrentStudent()
        val team = teamReader.read(teamId)
        teamValidator.validStageParticipant(student.studentId, team.game.stage.id)
        return teamReader.readTeamInfo(team)
    }

}
