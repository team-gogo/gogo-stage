package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchToggleDto
import gogo.gogostage.domain.match.root.application.dto.MatchInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchSearchDto

interface MatchService {
    fun matchApiInfo(matchId: Long): MatchApiInfoDto
    fun toggleMatchNotice(matchId: Long): MatchToggleDto
    fun search(stageId: Long, y: Int, m: Int, d: Int): MatchSearchDto
    fun info(matchId: Long): MatchInfoDto
    fun me(stageId: Long): MatchSearchDto
}
