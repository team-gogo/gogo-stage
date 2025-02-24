package gogo.gogostage.domain.match.result.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface MatchResultRepository: JpaRepository<MatchResult, Long> {
}
