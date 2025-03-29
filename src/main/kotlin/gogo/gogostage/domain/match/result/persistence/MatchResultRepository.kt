package gogo.gogostage.domain.match.result.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface MatchResultRepository: JpaRepository<MatchResult, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM MatchResult mr WHERE mr.match.id = :matchId")
    fun deleteByMatchId(matchId: Long)
}
