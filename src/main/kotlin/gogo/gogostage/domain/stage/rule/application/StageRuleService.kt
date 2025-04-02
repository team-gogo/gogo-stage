package gogo.gogostage.domain.stage.rule.application

import gogo.gogostage.domain.stage.rule.application.dto.StageRuleDto

interface StageRuleService {
    fun query(stageId: Long): StageRuleDto
}
