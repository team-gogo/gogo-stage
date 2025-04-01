package gogo.gogostage.domain.stage.rule.application

import gogo.gogostage.domain.stage.rule.persistence.StageRule
import gogo.gogostage.domain.stage.rule.persistence.StageRuleRepository
import org.springframework.stereotype.Component

@Component
class StageRuleReader(private val stageRuleRepository: StageRuleRepository) {

    fun readByStageId(stageId: String): StageRule = stageRuleRepository.findByStageId(stageId)

}
