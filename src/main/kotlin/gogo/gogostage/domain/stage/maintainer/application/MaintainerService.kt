package gogo.gogostage.domain.stage.maintainer.application

import gogo.gogostage.domain.stage.maintainer.application.dto.IsMaintainerDto

interface MaintainerService {
    fun isMaintainer(matchId: Long, studentId: Long): IsMaintainerDto
}
