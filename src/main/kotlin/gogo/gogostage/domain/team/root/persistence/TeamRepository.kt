package gogo.gogostage.domain.team.root.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository: JpaRepository<Team, Long>, TeamCustomRepository {
    fun findAllByGameIdAndIsParticipating(gameId: Long, isParticipating: Boolean): List<Team>
    fun findAllByGameId(gameId: Long): List<Team>
}
