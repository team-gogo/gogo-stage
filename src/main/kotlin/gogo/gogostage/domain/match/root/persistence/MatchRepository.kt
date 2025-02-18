package gogo.gogostage.domain.match.root.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MatchRepository: JpaRepository<Match, Long> {
    @Query("SELECT m FROM Match m WHERE m.id = :matchId AND m.isEnd = false")
    fun findNotEndMatchById(matchId: Long): Match?
}
