package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto

interface MatchService {
    fun matchApiInfo(matchId: Long): MatchApiInfoDto
}
