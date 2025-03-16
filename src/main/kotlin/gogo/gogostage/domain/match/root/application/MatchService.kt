package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchToggleDto

interface MatchService {
    fun matchApiInfo(matchId: Long): MatchApiInfoDto
    fun toggleMatchNotice(matchId: Long): MatchToggleDto
}
