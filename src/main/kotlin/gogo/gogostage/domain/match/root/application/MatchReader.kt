package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.match.root.persistence.MatchRepository
import gogo.gogostage.global.error.StageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class MatchReader(
    private val matchRepository: MatchRepository
) {

    fun read(matchId: Long): Match =
        matchRepository.findByIdOrNull(matchId)
            ?: throw StageException("Match not found, Match Id: $matchId", HttpStatus.NOT_FOUND.value())

    fun search(stageId: Long, studentId: Long, y: Int, m: Int, d: Int): List<Match> =
        matchRepository.search(stageId, studentId, y, m, d)

}
