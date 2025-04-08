package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.match.root.application.MatchReader
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainerRepository
import gogo.gogostage.domain.stage.participant.root.application.ParticipantReader
import gogo.gogostage.domain.stage.root.application.dto.*
import gogo.gogostage.domain.stage.root.event.*
import gogo.gogostage.domain.stage.root.persistence.StageRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.betting.api.BettingApi
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
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
    private val stageReader: StageReader,
    private val stageMaintainerRepository: StageMaintainerRepository,
    private val matchReader: MatchReader,
    private val bettingApi: BettingApi,
    private val participantReader: ParticipantReader
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
                    isCoinTossActive = stage.isActiveMiniGame,
                    coinTossMaxBettingPoint = dto.miniGame.coinToss.maxBettingPoint,
                    coinTossMinBettingPoint = dto.miniGame.coinToss.minBettingPoint,
                    coinTossInitialTicketCount = dto.miniGame.coinToss.initialTicketCount,
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

    @Transactional
    override fun confirm(stageId: Long, dto: StageConfirmDto) {
        val stage = stageRepository.queryNotEndStageById(stageId)
            ?: throw StageException("Stage Not Found, Stage Id = $stageId", HttpStatus.NOT_FOUND.value())

        val student = userUtil.getCurrentStudent()
        stageValidator.validConfirm(student, stage, dto)

        stageProcessor.confirm(stage, dto)

        applicationEventPublisher.publishEvent(
            StageConfirmEvent(
                id = UUID.randomUUID().toString(),
                stageId = stage.id,
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getPointRank(stageId: Long, page: Int, size: Int): StageParticipantPointRankDto {
        val student = userUtil.getCurrentStudent()
        val stage = stageReader.read(stageId)
        stageValidator.validStage(student, stage.id)
        return stageReader.readPointRank(stage, page, size)
    }

    @Transactional(readOnly = true)
    override fun queryAll(): QueryStageDto {
        val student = userUtil.getCurrentStudent()
        val stages = stageReader.readAllBySchoolId(student.schoolId)
        return stageMapper.mapAll(stages, student.studentId)
    }

    @Transactional(readOnly = true)
    override fun me(): QueryMyStageDto {
        val student = userUtil.getCurrentStudent()
        val stages = stageReader.readMy(student.studentId)
        return stageMapper.mapMy(stages, student.studentId)
    }

    @Transactional(readOnly = true)
    override fun checkMeStageMaintainer(stageId: Long): CheckStageMaintainerDto {
        val student = userUtil.getCurrentStudent()
        val stage = stageReader.read(stageId)
        val isMaintainer = stageMaintainerRepository.existsByStageIdAndStudentId(stage.id, student.studentId)
        return stageMapper.mapCheckMaintainerDto(isMaintainer)
    }

    @Transactional(readOnly = true)
    override fun wasted(stageId: Long): QueryMyWastedDto {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        val isWasted = stageProcessor.isWasted(student, stageId)
        return QueryMyWastedDto(isWasted)
    }

}
