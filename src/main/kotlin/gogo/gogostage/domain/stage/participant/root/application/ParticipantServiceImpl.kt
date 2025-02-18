package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.stage.participant.root.application.dto.PointDto
import org.springframework.stereotype.Service

@Service
class ParticipantServiceImpl(
    private val participantReader: ParticipantReader,
    private val participantMapper: ParticipantMapper
) : ParticipantService {

    override fun queryPoint(stageId: Long, studentId: Long): PointDto {
        val participant = participantReader.read(stageId, studentId)
        return participantMapper.mapPoint(participant)
    }

}
