package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.game.persistence.GameRepository
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import gogo.gogostage.domain.stage.root.application.dto.StageConfirmDto
import gogo.gogostage.domain.stage.root.application.dto.StageJoinDto
import gogo.gogostage.domain.stage.root.persistence.Stage
import gogo.gogostage.domain.stage.root.persistence.StageRepository
import gogo.gogostage.domain.stage.root.persistence.StageStatus
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class StageValidator(
    private val stageParticipantRepository: StageParticipantRepository,
    private val gameRepository: GameRepository,
    private val stageRepository: StageRepository
) {

    fun validStage(student: StudentByIdStub, stageId: Long) {
        val stage = (stageRepository.findByIdOrNull(stageId)
            ?: throw StageException("Stage Not Found, Stage Id = $stageId", HttpStatus.NOT_FOUND.value()))

        val isParticipate = stageParticipantRepository.existsByStageIdAndStudentId(stage.id, student.studentId)

        if (student.schoolId != stage.schoolId || isParticipate.not()) {
            throw StageException("해당 스테이지에 참여하지 않았습니다.", HttpStatus.FORBIDDEN.value())
        }
    }

    fun validFast(dto: CreateFastStageDto) {
        if (dto.maintainer.size > 5) {
            throw StageException("스테이지 관리자는 최대 5명까지 가능합니다.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.miniGame.coinToss.isActive && (dto.miniGame.coinToss.maxBettingPoint == null || dto.miniGame.coinToss.initialTicketCount == null)) {
            throw StageException("CoinToss 미니게임의 최대 배팅 포인트, 초기 보유 티켓 개수를 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }
    }

    fun validOfficial(dto: CreateOfficialStageDto) {
        if (dto.maintainer.size > 5) {
            throw StageException("스테이지 관리자는 최대 5명까지 가능합니다.", HttpStatus.BAD_REQUEST.value())
        }

        validMiniGame(dto)
        validShop(dto)
    }

    fun validJoin(student: StudentByIdStub, dto: StageJoinDto, stage: Stage) {
        if (stage.passCode != null && stage.passCode != dto.passCode) {
            throw StageException("입장 코드가 올바르지 않습니다.", HttpStatus.BAD_REQUEST.value())
        }

        val isDuplicate = stageParticipantRepository.existsByStageIdAndStudentId(stage.id, student.studentId)
        if (isDuplicate) {
            throw StageException("이미 해당 스테이지에 참여 했습니다.", HttpStatus.BAD_REQUEST.value())
        }

        val isSchool = stage.schoolId == student.schoolId
        if (isSchool) {
            throw StageException("해당 스테이지는 현재 소속 중인 학교의 스테이지가 아닙니다.", HttpStatus.BAD_REQUEST.value())
        }
    }

    fun validConfirm(student: StudentByIdStub, stage: Stage, dto: StageConfirmDto) {
        val isMaintainer = stage.maintainer.any{ it.studentId == student.studentId }
        if (isMaintainer.not()) {
            throw StageException("해당 스테이지의 관리자가 아닙니다.", HttpStatus.FORBIDDEN.value())
        }

        val stageStatus = stage.status
        if (stageStatus != StageStatus.RECRUITING) {
            throw StageException("해당 스테이지는 모집 중인 스테이지가 아닙니다.", HttpStatus.BAD_REQUEST.value())
        }

        val gameCount = gameRepository.countByStageId(stage.id)
        if (gameCount != dto.games.size) {
            throw StageException("해당 스테이지의 게임 수와 확정된 게임 수가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value())
        }
    }

    private fun validShop(dto: CreateOfficialStageDto) {
        if (dto.shop.coinToss.isActive && (dto.shop.coinToss.price == null || dto.shop.coinToss.quantity == null)) {
            throw StageException("CoinToss 미니게임의 티켓 가격, 수량을 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.shop.yavarwee.isActive && (dto.shop.yavarwee.price == null || dto.shop.yavarwee.quantity == null)) {
            throw StageException("Yavarwee 미니게임의 티켓 가격, 수량을 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.shop.plinko.isActive && (dto.shop.plinko.price == null || dto.shop.plinko.quantity == null)) {
            throw StageException("Plinko 미니게임의 티켓 가격, 수량을 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.miniGame.coinToss.isActive.not() && dto.shop.coinToss.isActive) {
            throw StageException("CoinToss 미니게임을 활성화 하지않은 상태에서 상점을 활성화 할 수 없습니다.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.miniGame.yavarwee.isActive.not() && dto.shop.yavarwee.isActive) {
            throw StageException("Yavarwee 미니게임을 활성화 하지않은 상태에서 상점을 활성화 할 수 없습니다.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.miniGame.plinko.isActive.not() && dto.shop.plinko.isActive) {
            throw StageException("Plinko 미니게임을 활성화 하지않은 상태에서 상점을 활성화 할 수 없습니다.", HttpStatus.BAD_REQUEST.value())
        }
    }

    private fun validMiniGame(dto: CreateOfficialStageDto) {
        if (dto.miniGame.coinToss.isActive && (dto.miniGame.coinToss.maxBettingPoint == null || dto.miniGame.coinToss.initialTicketCount == null)) {
            throw StageException("CoinToss 미니게임의 최대 배팅 포인트, 초기 보유 티켓 개수를 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.miniGame.yavarwee.isActive && (dto.miniGame.yavarwee.maxBettingPoint == null || dto.miniGame.yavarwee.initialTicketCount == null)) {
            throw StageException("Yavarwee 미니게임의 최대 배팅 포인트, 초기 보유 티켓 개수를 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.miniGame.plinko.isActive && (dto.miniGame.plinko.maxBettingPoint == null || dto.miniGame.plinko.initialTicketCount == null)) {
            throw StageException("Plinko 미니게임의 최대 배팅 포인트, 초기 보유 티켓 개수를 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }
    }

}
