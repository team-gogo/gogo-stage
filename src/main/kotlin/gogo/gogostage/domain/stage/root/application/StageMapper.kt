package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import gogo.gogostage.domain.stage.root.event.CreateStageOfficialEvent
import gogo.gogostage.domain.stage.root.event.OfficialStageMiniGameDto
import gogo.gogostage.domain.stage.root.event.OfficialStageShopDto
import gogo.gogostage.domain.stage.root.event.OfficialStageShopInfoDto
import gogo.gogostage.domain.stage.root.persistence.Stage
import org.springframework.stereotype.Component
import java.util.*

@Component
class StageMapper {

    fun mapCreateOfficialEvent(
        stage: Stage,
        dto: CreateOfficialStageDto
    ) = CreateStageOfficialEvent(
        id = UUID.randomUUID().toString(),
        stageId = stage.id,
        miniGame = OfficialStageMiniGameDto(
            isCoinTossActive = dto.miniGame.coinToss.isActive,
            isYavarweeActive = dto.miniGame.yavarwee.isActive,
            isPlinkoActive = dto.miniGame.plinko.isActive
        ),
        shop = OfficialStageShopDto(
            coinToss = OfficialStageShopInfoDto(
                isActive = dto.shop.coinToss.isActive,
                price = dto.shop.coinToss.price,
                quantity = dto.shop.coinToss.quantity,
            ),
            yavarwee = OfficialStageShopInfoDto(
                isActive = dto.shop.yavarwee.isActive,
                price = dto.shop.yavarwee.price,
                quantity = dto.shop.yavarwee.quantity,
            ),
            plinko = OfficialStageShopInfoDto(
                isActive = dto.shop.plinko.isActive,
                price = dto.shop.plinko.price,
                quantity = dto.shop.plinko.quantity,
            ),
        )
    )

}
