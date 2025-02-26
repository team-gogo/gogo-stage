package gogo.gogostage.domain.stage.maintainer.persistence

import gogo.gogostage.domain.stage.root.persistence.Stage
import org.springframework.data.jpa.repository.JpaRepository

interface StageMaintainerRepository: JpaRepository<StageMaintainer, Long> {
    fun findByStage(stage: Stage): List<StageMaintainer>
}
