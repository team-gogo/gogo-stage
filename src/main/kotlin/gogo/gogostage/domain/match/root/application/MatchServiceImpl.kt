package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchToggleDto
import gogo.gogostage.global.util.UserContextUtil
import gogo.gogostage.domain.match.root.application.dto.MatchInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchSearchDto
import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.global.cache.CacheConstant
import gogo.gogostage.global.internal.betting.api.BettingApi
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MatchServiceImpl(
    private val matchReader: MatchReader,
    private val matchProcessor: MatchProcessor,
    private val matchMapper: MatchMapper,
    private val userUtil: UserContextUtil,
    private val stageValidator: StageValidator,
    private val bettingApi: BettingApi
) : MatchService {

    @Transactional(readOnly = true)
    override fun matchApiInfo(matchId: Long): MatchApiInfoDto {
        val match = matchReader.read(matchId)
        return matchMapper.mapApiInfo(match)
    }

    @Transactional(readOnly = true)
    override fun search(stageId: Long, y: Int, m: Int, d: Int): MatchSearchDto {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        val matches = matchReader.search(stageId, student.studentId, y, m, d)
        return matchMapper.mapSearch(matches, student.studentId)
    }

    @Transactional(readOnly = true)
    @Cacheable(value = [CacheConstant.MATCH_INFO_CACHE_VALUE], key = "#matchId")
    override fun info(matchId: Long): MatchInfoDto {
        val student = userUtil.getCurrentStudent()
        val match = matchReader.info(matchId)
        stageValidator.validStage(student, match.game.stage.id)
        return matchMapper.mapInfo(match)
    }

    @Transactional
    override fun toggleMatchNotice(matchId: Long): MatchToggleDto {
        val student = userUtil.getCurrentStudent()
        val match = matchReader.read(matchId)
        return matchProcessor.toggleMatchNotification(match, student)
    }

    @Transactional(readOnly = true)
    override fun me(stageId: Long): MatchSearchDto {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        val allMatches = matchReader.readByStageId(stageId)
        val bettingBundle = bettingApi.bundle(allMatches.map{ it.id }, student.studentId)
        val betMatches = matchReader.readMy(bettingBundle.bettings.map { it.matchId })
        return matchMapper.mapMy(bettingBundle, betMatches, student.studentId)
    }

}
