package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import gogo.gogostage.domain.stage.root.application.dto.StageJoinDto
import gogo.gogostage.domain.stage.root.event.*
import gogo.gogostage.domain.stage.root.persistence.StageRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class StageServiceImpl(
    private val userUtil: UserContextUtil,
    private val stageProcessor: StageProcessor,
    private val stageValidator: StageValidator,
    private val stageMapper: StageMapper,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val stageRepository: StageRepository,
) : StageService {

    @Transactional
    override fun createFast(dto: CreateFastStageDto) {
        val student = userUtil.getCurrentStudent()
        stageValidator.validFast(dto)
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
        stageValidator.validOfficial(dto)
        val stage = stageProcessor.saveOfficial(student, dto)

        val event = stageMapper.mapCreateOfficialEvent(stage, dto)
        applicationEventPublisher.publishEvent(event)
    }

    @Transactional
    override fun join(stageId: Long, dto: StageJoinDto) {
        val stage = stageRepository.queryNotEndStageById(stageId)
            ?: throw StageException("Stage Not Found, Stage Id = $stageId", HttpStatus.NOT_FOUND.value())

        val student = userUtil.getCurrentStudent()
        stageValidator.validJoin(student, dto, stage)

        stageProcessor.join(student, stage)
    }

}
