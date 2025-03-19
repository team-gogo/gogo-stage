package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.stage.participant.root.application.dto.IsParticipantDto
import gogo.gogostage.domain.stage.participant.root.application.dto.PointDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ParticipantServiceImpl(
    private val participantReader: ParticipantReader,
    private val participantMapper: ParticipantMapper
) : ParticipantService {

    @Transactional(readOnly = true)
    override fun queryPoint(stageId: Long, studentId: Long): PointDto {
        val participant = participantReader.read(stageId, studentId)
        return participantMapper.mapPoint(participant)
    }

    @Transactional(readOnly = true)
    override fun isParticipant(stageId: Long, studentId: Long): IsParticipantDto {
        val isParticipant = participantReader.isParticipant(stageId, studentId)
        return IsParticipantDto(isParticipant)
    }

}
