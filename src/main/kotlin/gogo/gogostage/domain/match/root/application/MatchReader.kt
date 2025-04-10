package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.match.root.persistence.MatchRepository
import gogo.gogostage.global.error.StageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class MatchReader(
    private val matchRepository: MatchRepository,
) {

    fun read(matchId: Long): Match =
        matchRepository.findByIdOrNull(matchId)
            ?: throw StageException("Match not found, Match Id: $matchId", HttpStatus.NOT_FOUND.value())

    fun readByStageId(stageId: Long): List<Match> = matchRepository.findByStageId(stageId)

    fun readNotEndByStageId(stageId: Long): List<Match> = matchRepository.findAllNotEndMatchByStageId(stageId)

    fun readMy(matchIds: List<Long>): List<Match> = matchRepository.findMy(matchIds)

    fun search(stageId: Long, studentId: Long, y: Int, m: Int, d: Int): List<Match> =
        matchRepository.search(stageId, studentId, y, m, d)

    fun info(matchId: Long): Match =
        matchRepository.info(matchId)
            ?: throw StageException("Match not found, Match Id: $matchId", HttpStatus.NOT_FOUND.value())

    fun readAllByGameId(gameId: Long): List<Match> = matchRepository.findAllByGameId(gameId)

}
