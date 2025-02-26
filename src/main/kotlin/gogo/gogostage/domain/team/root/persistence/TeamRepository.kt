package gogo.gogostage.domain.team.root.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository: JpaRepository<Team, Long> {
}