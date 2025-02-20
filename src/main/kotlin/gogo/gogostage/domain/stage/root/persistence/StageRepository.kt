package gogo.gogostage.domain.stage.root.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface StageRepository: JpaRepository<Stage, Long> {
}
