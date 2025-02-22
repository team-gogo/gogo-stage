package gogo.gogostage.domain.stage.maintainer.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface StageMaintainerRepository: JpaRepository<StageMaintainer, Long> {
    fun existsByStageIdAndStudentId(stageId: Long, studentId: Long): Boolean
}
