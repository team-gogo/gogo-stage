package gogo.gogostage.domain.game.application

import gogo.gogostage.domain.game.application.dto.QueryGameDto
import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameServiceImpl(
    private val gameReader: GameReader,
    private val userUtil: UserContextUtil,
    private val stageValidator: StageValidator,
    private val gameMapper: GameMapper
) : GameService {

    @Transactional(readOnly = true)
    override fun queryAll(stageId: Long): QueryGameDto {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        val games = gameReader.readByStageId(stageId)
        return gameMapper.mapAll(games)
    }

}
