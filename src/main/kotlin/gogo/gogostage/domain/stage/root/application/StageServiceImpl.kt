package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainer
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainerRepository
import gogo.gogostage.domain.stage.minigameinfo.persistence.MiniGameInfo
import gogo.gogostage.domain.stage.minigameinfo.persistence.MiniGameInfoRepository
import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.persistence.Stage
import gogo.gogostage.domain.stage.root.persistence.StageRepository
import gogo.gogostage.domain.stage.rule.persistence.StageRule
import gogo.gogostage.domain.stage.rule.persistence.StageRuleRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StageServiceImpl(
    private val userUtil: UserContextUtil,
    private val stageRepository: StageRepository,
    private val miniGameInfoRepository: MiniGameInfoRepository,
    private val stageRuleRepository: StageRuleRepository,
    private val stageMaintainerRepository: StageMaintainerRepository,
) : StageService {

    @Transactional
    override fun createFast(dto: CreateFastStageDto) {
        val student = userUtil.getCurrentStudent()
        val isActiveCoinToss = dto.miniGame.coinToss.isActive

        val stage = Stage.fastOf(student.schoolId, dto, isActiveCoinToss)
        stageRepository.save(stage)

        val miniGameInfo = MiniGameInfo.ofFast(stage, isActiveCoinToss)
        miniGameInfoRepository.save(miniGameInfo)

        val stageRule = StageRule.of(stage, dto.rule)
        stageRuleRepository.save(stageRule)

        if (dto.maintainer.size > 5) {
            throw StageException("스테이지 관리자는 최대 5명까지 가능합니다.", HttpStatus.BAD_REQUEST.value())
        }

        val maintainers = dto.maintainer.map { StageMaintainer.of(stage, it) } + StageMaintainer.of(stage, student.studentId)
        stageMaintainerRepository.saveAll(maintainers)

        // create_stage_fast 이벤트 발행
    }

}
