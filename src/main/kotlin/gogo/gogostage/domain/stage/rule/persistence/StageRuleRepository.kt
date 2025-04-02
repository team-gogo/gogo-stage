package gogo.gogostage.domain.stage.rule.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StageRuleRepository : JpaRepository<StageRule, Long> {
    @Query("SELECT sr FROM StageRule sr WHERE sr.stage.id = :stageId")
    fun findByStageId(stageId: Long): StageRule
}
