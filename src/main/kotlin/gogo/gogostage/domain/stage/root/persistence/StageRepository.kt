package gogo.gogostage.domain.stage.root.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StageRepository: JpaRepository<Stage, Long>, StageCustomRepository {
    @Query("SELECT s FROM Stage s WHERE s.status <> :status AND s.id = :stageId")
    fun queryNotEndStageById(stageId: Long, status: StageStatus = StageStatus.END): Stage?
}
