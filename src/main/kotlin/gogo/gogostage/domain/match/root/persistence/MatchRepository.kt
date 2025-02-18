package gogo.gogostage.domain.match.root.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface MatchRepository: JpaRepository<Match, Long> {
}
