package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class StageValidator {

    fun validFast(dto: CreateFastStageDto) {
        if (dto.maintainer.size > 5) {
            throw StageException("스테이지 관리자는 최대 5명까지 가능합니다.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.miniGame.coinToss.isActive && dto.miniGame.coinToss.maxBettingPoint == null) {
            throw StageException("CoinToss 미니게임의 최대 배팅 포인트를 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }
    }

    fun validOfficial(dto: CreateOfficialStageDto) {
        if (dto.maintainer.size > 5) {
            throw StageException("스테이지 관리자는 최대 5명까지 가능합니다.", HttpStatus.BAD_REQUEST.value())
        }

        validMiniGame(dto)
        validShop(dto)
    }

    private fun validShop(dto: CreateOfficialStageDto) {
        if (dto.shop.coinToss.isActive && dto.shop.coinToss.price == null || dto.shop.coinToss.quantity == null) {
            throw StageException("CoinToss 미니게임의 티켓 가격, 수량을 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.shop.yavarwee.isActive && dto.shop.yavarwee.price == null || dto.shop.yavarwee.quantity == null) {
            throw StageException("Yavarwee 미니게임의 티켓 가격, 수량을 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.shop.plinko.isActive && dto.shop.plinko.price == null || dto.shop.plinko.quantity == null) {
            throw StageException("Plinko 미니게임의 티켓 가격, 수량을 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }
    }

    private fun validMiniGame(dto: CreateOfficialStageDto) {
        if (dto.miniGame.coinToss.isActive && dto.miniGame.coinToss.maxBettingPoint == null) {
            throw StageException("CoinToss 미니게임의 최대 배팅 포인트를 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.miniGame.yavarwee.isActive && dto.miniGame.yavarwee.maxBettingPoint == null) {
            throw StageException("Yavarwee 미니게임의 최대 배팅 포인트를 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }

        if (dto.miniGame.plinko.isActive && dto.miniGame.plinko.maxBettingPoint == null) {
            throw StageException("Plinko 미니게임의 최대 배팅 포인트를 입력하세요.", HttpStatus.BAD_REQUEST.value())
        }
    }

}
