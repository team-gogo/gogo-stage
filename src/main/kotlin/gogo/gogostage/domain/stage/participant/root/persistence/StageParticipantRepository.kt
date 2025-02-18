package gogo.gogostage.domain.stage.participant.root.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface StageParticipantRepository: JpaRepository<StageParticipant, Long> {
    fun findByStageIdAndStudentId(stageId: Long, studentId: Long): StageParticipant?
}
