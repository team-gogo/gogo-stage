package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.CreateStageRuleDto

interface StageService {
    fun createFast(dto: CreateStageRuleDto)
}
