package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.domain.stage.root.application.StageService
import gogo.gogostage.domain.stage.root.persistence.StageStatus
import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto
import gogo.gogostage.domain.team.root.persistence.TeamRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class TeamValidator(
    private val stageParticipantRepository: StageParticipantRepository,
    private val teamRepository: TeamRepository
) {

    fun validApply(student: StudentByIdStub, game: Game, dto: TeamApplyDto) {
        val isParticipant = stageParticipantRepository.existsByStageIdAndStudentId(
            stageId = game.stage.id,
            studentId = student.studentId
        )
        if (isParticipant.not()) {
            throw StageException("스테이지 참여자가 아닙니다.", HttpStatus.FORBIDDEN.value())
        }

        val stageStatus = game.stage.status
        if (stageStatus != StageStatus.RECRUITING) {
            throw StageException("스테이지가 모집 중이 아닙니다.", HttpStatus.FORBIDDEN.value())
        }

        val teamSize = dto.participants.size
        if (teamSize < game.teamMinCapacity || teamSize > game.teamMaxCapacity) {
            throw StageException("해당 경기의 팀 인원이 맞지 않습니다.", HttpStatus.BAD_REQUEST.value())
        }

        val participantIds = teamRepository.findAllByGameId(game.id)
            .flatMap { it.participants.map { participant -> participant.studentId } }
            .toList()

        val isDuplicate = dto.participants.any { it.studentId in participantIds }
        if (isDuplicate) {
            throw StageException("이미 등록된 학생이 있습니다.", HttpStatus.BAD_REQUEST.value())
        }
    }

    fun validStageParticipant(studentId: Long, stageId: Long) {
        val isParticipant =
            stageParticipantRepository.existsByStageIdAndStudentId(stageId, studentId)
        if (isParticipant.not()) {
            throw StageException("스테이지 참여자가 아닙니다.", HttpStatus.FORBIDDEN.value())
        }
    }

}
