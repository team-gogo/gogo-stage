package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.game.application.GameReader
import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.domain.team.root.application.dto.GameTeamResDto
import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto
import gogo.gogostage.global.cache.CacheConstant
import gogo.gogostage.domain.team.root.application.dto.TeamInfoDto
import gogo.gogostage.global.internal.student.api.StudentApi
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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
    private val stageValidator: StageValidator,
    private val studentApi: StudentApi
) : TeamService {

    @Transactional
    @CacheEvict(value = [CacheConstant.TEMP_TEAM_CACHE_VALUE], key = "#gameId", cacheManager = "cacheManager")
    override fun apply(gameId: Long, dto: TeamApplyDto) {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        teamValidator.validApply(student, game, dto)

        teamProcessor.apply(game, dto)
    }

    @Transactional(readOnly = true)
    @Cacheable(value = [CacheConstant.TEAM_CACHE_VALUE], key = "#gameId", cacheManager = "cacheManager")
    override fun getGameTeam(gameId: Long): GameTeamResDto {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        stageValidator.validStage(student, game.stage.id)
        val teams = teamReader.readParticipatingTeamByGameId(game.id, true)
        return teamMapper.mapGameTeam(teams)
    }

    @Transactional(readOnly = true)
    @Cacheable(value = [CacheConstant.TEMP_TEAM_CACHE_VALUE], key = "#gameId", cacheManager = "cacheManager")
    override fun getGameTempTeam(gameId: Long): GameTeamResDto {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        stageValidator.validStage(student, game.stage.id)
        val tempTeams = teamReader.readParticipatingTeamByGameId(game.id, false)
        return teamMapper.mapGameTeam(tempTeams)
    }

    @Transactional(readOnly = true)
    @Cacheable(value = [CacheConstant.TEAM_INFO_CACHE_VALUE], key = "#teamId", cacheManager = "cacheManager")
    override fun getTeamInfo(teamId: Long): TeamInfoDto {
        val student = userUtil.getCurrentStudent()
        val team = teamReader.read(teamId)
        stageValidator.validStage(student, team.game.stage.id)
        val studentIds = teamMapper.mapStudentIds(team)
        val students = studentApi.queryByStudentsIds(studentIds).students
        val participantDto = teamMapper.mapParticipantInfoDto(team, students)
        return teamMapper.mapTeamInfoDto(team, participantDto)
    }

}
