package gogo.gogostage.domain.match.root.persistence

import gogo.gogostage.domain.match.root.application.dto.MatchSearchDto

interface MatchCustomRepository {
    fun search(stageId: Long, studentId: Long, y: Int, m: Int, d: Int): MatchSearchDto
}
