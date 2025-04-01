package gogo.gogostage.domain.stage.rule.application

import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.domain.stage.root.persistence.QStage.stage
import gogo.gogostage.domain.stage.rule.application.dto.StageRuleDto
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StageRuleServiceImpl(
    private val stageRuleReader: StageRuleReader,
    private val stageRuleMapper: StageRuleMapper,
    private val userUtil: UserContextUtil,
    private val stageValidator: StageValidator
) : StageRuleService {

    @Transactional(readOnly = true)
    override fun query(stageId: Long): StageRuleDto {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        val rule = stageRuleReader.readByStageId(stageId)
        return stageRuleMapper.map(rule)
    }

}
