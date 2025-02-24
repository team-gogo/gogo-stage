package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MatchServiceImpl(
    private val matchReader: MatchReader,
    private val matchMapper: MatchMapper
) : MatchService {

    @Transactional(readOnly = true)
    override fun matchApiInfo(matchId: Long): MatchApiInfoDto {
        val match = matchReader.read(matchId)
        return matchMapper.mapInfo(match)
    }

}
