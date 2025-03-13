package gogo.gogostage.domain.team.root.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TeamRepository: JpaRepository<Team, Long> {
    fun findAllByGameId(gameId: Long): List<Team>

    @Query("SELECT t FROM Team t WHERE t.game.id = :gameId ORDER BY t.winCount DESC LIMIT 3")
    fun findTop3Teams(gameId: Long): List<Team>
}
