package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.stage.participant.root.application.dto.PointDto
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipant
import org.springframework.stereotype.Component

@Component
class ParticipantMapper {

    fun mapPoint(participant: StageParticipant) = PointDto(participant.point)

}
