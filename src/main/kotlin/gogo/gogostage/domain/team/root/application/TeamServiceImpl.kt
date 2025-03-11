package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.game.application.GameReader
import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TeamServiceImpl(
    private val userUtil: UserContextUtil,
    private val gameReader: GameReader,
    private val teamValidator: TeamValidator,
    private val teamProcessor: TeamProcessor
) : TeamService {

    @Transactional
    override fun apply(gameId: Long, dto: TeamApplyDto) {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        teamValidator.validApply(student, game, dto)

        teamProcessor.apply(game, dto)
    }

}
