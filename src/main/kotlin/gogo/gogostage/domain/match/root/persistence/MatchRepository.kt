package gogo.gogostage.domain.match.root.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MatchRepository: JpaRepository<Match, Long>, MatchCustomRepository {
    @Query("SELECT m FROM Match m WHERE m.id = :matchId AND m.isEnd = false")
    fun findNotEndMatchById(matchId: Long): Match?

    @Query("SELECT m FROM Match m WHERE m.game.stage.id = :stageId")
    fun findByStageId(stageId: Long): List<Match>

    fun findByGameIdAndRoundAndTurn(gameId: Long, round: Round, turn: Int): Match?

    @Query("""
            SELECT DISTINCT m 
            FROM Match m 
            LEFT JOIN FETCH m.matchResult mr 
            LEFT JOIN FETCH m.game g 
            LEFT JOIN FETCH g.stage s 
            LEFT JOIN FETCH m.aTeam at 
            LEFT JOIN FETCH m.bTeam bt 
            WHERE m.id IN (:matchIds)
       """)
    fun findMy(matchIds: List<Long>): List<Match>
}
