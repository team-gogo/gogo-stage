package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto

interface StageService {
    fun createFast(dto: CreateFastStageDto)
    fun createOfficial(dto: CreateOfficialStageDto)
}
