package gogo.gogostage.domain.game.application

import gogo.gogostage.domain.game.application.dto.QueryGameDto
import gogo.gogostage.domain.game.application.dto.QueryGameFormatDto
import gogo.gogostage.domain.match.root.application.MatchReader
import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.global.cache.CacheConstant
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameServiceImpl(
    private val gameReader: GameReader,
    private val userUtil: UserContextUtil,
    private val stageValidator: StageValidator,
    private val gameMapper: GameMapper,
    private val gameValidator: GameValidator,
    private val matchReader: MatchReader
) : GameService {

    @Transactional(readOnly = true)
    @Cacheable(value = [CacheConstant.STAGE_GAME_CACHE_VALE], key = "#stageId", cacheManager = "cacheManager")
    override fun queryAll(stageId: Long): QueryGameDto {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        val games = gameReader.readByStageId(stageId)
        return gameMapper.mapAll(games)
    }

    @Transactional(readOnly = true)
    override fun queryFormat(gameId: Long): QueryGameFormatDto {
        val student = userUtil.getCurrentStudent()
        val game = gameReader.read(gameId)
        stageValidator.validStage(student, game.stage.id)
        gameValidator.validFormat(game)
        val matches = matchReader.readAllByGameId(gameId)
        return gameMapper.mapFormat(matches)
    }

}
