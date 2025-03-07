package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import gogo.gogostage.domain.stage.root.event.CreateStageFastEvent
import gogo.gogostage.domain.stage.root.event.CreateStageOfficialEvent
import gogo.gogostage.domain.stage.root.event.FastStageMiniGameDto
import gogo.gogostage.domain.stage.root.event.OfficialStageMiniGameInfoDto
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class StageServiceImpl(
    private val userUtil: UserContextUtil,
    private val stageProcessor: StageProcessor,
    private val stageValidator: StageValidator,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : StageService {

    @Transactional
    override fun createFast(dto: CreateFastStageDto) {
        val student = userUtil.getCurrentStudent()
        stageValidator.valid(dto.maintainer)
        val stage = stageProcessor.saveFast(student, dto)

        applicationEventPublisher.publishEvent(
            CreateStageFastEvent(
                id = UUID.randomUUID().toString(),
                stageId = stage.id,
                miniGame = FastStageMiniGameDto(
                    isCoinTossActive = stage.isActiveMiniGame
                )
            )
        )
    }

    @Transactional
    override fun createOfficial(dto: CreateOfficialStageDto) {
        val student = userUtil.getCurrentStudent()
        stageValidator.valid(dto.maintainer)
        val stage = stageProcessor.saveOfficial(student, dto)

        applicationEventPublisher.publishEvent(
            CreateStageOfficialEvent(
                id = UUID.randomUUID().toString(),
                stageId = stage.id,
                miniGame = OfficialStageMiniGameInfoDto(
                    isCoinTossActive = dto.miniGame.coinToss.isActive,
                    isYavarweeActive = dto.miniGame.yavarwee.isActive,
                    isPlinkoActive = dto.miniGame.plinko.isActive
                ),
                shop = OfficialStageMiniGameInfoDto(
                    isCoinTossActive = dto.shop.coinToss.isActive,
                    isYavarweeActive = dto.shop.yavarwee.isActive,
                    isPlinkoActive = dto.shop.plinko.isActive
                )
            )
        )
    }

}
