package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchToggleDto
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MatchServiceImpl(
    private val matchReader: MatchReader,
    private val matchMapper: MatchMapper,
    private val userUtil: UserContextUtil,
    private val matchProcessor: MatchProcessor
) : MatchService {

    @Transactional(readOnly = true)
    override fun matchApiInfo(matchId: Long): MatchApiInfoDto {
        val match = matchReader.read(matchId)
        return matchMapper.mapInfo(match)
    }

    @Transactional
    override fun toggleMatchNotice(matchId: Long): MatchToggleDto {
        val student = userUtil.getCurrentStudent()
        val match = matchReader.read(matchId)
        return matchProcessor.toggleMatchNotification(match, student)
    }

}
