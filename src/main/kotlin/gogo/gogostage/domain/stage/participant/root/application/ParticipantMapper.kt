package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.stage.participant.root.application.dto.MyTempPointDto
import gogo.gogostage.domain.stage.participant.root.application.dto.PointDto
import gogo.gogostage.domain.stage.participant.root.application.dto.TempPointDto
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipant
import gogo.gogostage.domain.stage.participant.temppoint.persistence.TempPoint
import org.springframework.stereotype.Component

@Component
class ParticipantMapper {

    fun mapPoint(participant: StageParticipant) = PointDto(participant.point)

    fun mapMyTempPointDto(tempPoint: List<TempPoint>): MyTempPointDto {
        val tempPointDtoList = tempPoint.map {
            TempPointDto(
                tempPointId = it.id,
                tempPoint = it.tempPoint,
                expiredDate = it.tempPointExpiredDate
            )
        }

        return MyTempPointDto(tempPointDtoList)
    }
}
