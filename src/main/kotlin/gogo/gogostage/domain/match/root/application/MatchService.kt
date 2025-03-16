package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchSearchDto

interface MatchService {
    fun matchApiInfo(matchId: Long): MatchApiInfoDto
    fun search(stageId: Long, y: Int, m: Int, d: Int): MatchSearchDto
}
