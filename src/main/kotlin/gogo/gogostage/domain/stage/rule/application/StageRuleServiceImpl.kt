package gogo.gogostage.domain.stage.rule.application

import gogo.gogostage.domain.stage.rule.application.dto.StageRuleDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StageRuleServiceImpl(
    private val stageRuleReader: StageRuleReader,
    private val stageRuleMapper: StageRuleMapper
) : StageRuleService {

    @Transactional(readOnly = true)
    override fun query(stageId: String): StageRuleDto {
        val rule = stageRuleReader.readByStageId(stageId)
        return stageRuleMapper.map(rule)
    }

}
