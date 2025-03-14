package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import gogo.gogostage.domain.stage.root.application.dto.StageConfirmDto
import gogo.gogostage.domain.stage.root.application.dto.StageJoinDto

interface StageService {
    fun createFast(dto: CreateFastStageDto)
    fun createOfficial(dto: CreateOfficialStageDto)
    fun join(stageId: Long, dto: StageJoinDto)
    fun confirm(stageId: Long, dto: StageConfirmDto)
}
