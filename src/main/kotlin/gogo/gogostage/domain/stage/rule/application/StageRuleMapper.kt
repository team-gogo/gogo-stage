package gogo.gogostage.domain.stage.rule.application

import gogo.gogostage.domain.stage.rule.application.dto.StageRuleDto
import gogo.gogostage.domain.stage.rule.persistence.StageRule
import org.springframework.stereotype.Component

@Component
class StageRuleMapper {

    fun map(stageRule: StageRule): StageRuleDto = StageRuleDto(
        maxBettingPoint = stageRule.maxBettingPoint,
        minBettingPoint = stageRule.minBettingPoint,
    )

}
