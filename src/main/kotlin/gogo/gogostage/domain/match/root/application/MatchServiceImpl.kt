package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchSearchDto
import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MatchServiceImpl(
    private val matchReader: MatchReader,
    private val matchMapper: MatchMapper,
    private val userUtil: UserContextUtil,
    private val stageValidator: StageValidator
) : MatchService {

    @Transactional(readOnly = true)
    override fun matchApiInfo(matchId: Long): MatchApiInfoDto {
        val match = matchReader.read(matchId)
        return matchMapper.mapInfo(match)
    }

    @Transactional(readOnly = true)
    override fun search(stageId: Long, y: Int, m: Int, d: Int): MatchSearchDto {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        return matchReader.search(stageId, student.studentId, y, m, d)
    }

}
