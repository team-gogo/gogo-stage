package gogo.gogostage.domain.stage.participant.root.application

import gogo.gogostage.domain.stage.participant.root.application.dto.MyPointDto
import gogo.gogostage.domain.stage.participant.root.application.dto.MyTempPointDto
import gogo.gogostage.domain.stage.participant.root.application.dto.PointDto

interface ParticipantService {
    fun queryPoint(stageId: Long, studentId: Long): PointDto
    fun getMyTempPoint(stageId: Long): MyTempPointDto
    fun getMyPoint(stageId: Long): MyPointDto
}
