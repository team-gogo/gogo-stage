package gogo.gogostage.domain.stage.rule.application

import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.domain.stage.rule.application.dto.StageRuleDto
import gogo.gogostage.global.cache.CacheConstant
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.cache.annotation.Cacheable
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
    @Cacheable(value = [CacheConstant.STAGE_RULE_CACHE_VALE], key = "#stageId", cacheManager = "cacheManager")
    override fun query(stageId: Long): StageRuleDto {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        val rule = stageRuleReader.readByStageId(stageId)
        return stageRuleMapper.map(rule)
    }

}
