package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto

interface StageService {
    fun createFast(dto: CreateFastStageDto)
}
