package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.*
import gogo.gogostage.domain.stage.root.event.*
import gogo.gogostage.domain.stage.root.persistence.Stage
import org.springframework.stereotype.Component
import java.util.*

@Component
class StageMapper {

    fun mapAll(stages: List<Stage>, studentId: Long): QueryStageDto =
        QueryStageDto(
            count = stages.size,
            stages.map {
                QueryStageInfoDto(
                    stageId = it.id,
                    stageName = it.name,
                    type = it.type,
                    status = it.status,
                    participantCount = it.participantCount,
                    isParticipating = it.participant.any { p -> p.studentId == studentId },
                    isMaintainer = it.maintainer.any { p -> p.studentId == studentId },
                    isPassCode = it.passCode.isNullOrBlank().not(),
                )
            }
        )

    fun mapMy(stages: List<Stage>, studentId: Long): QueryMyStageDto =
        QueryMyStageDto(
            stages.map {
                QueryMyStageInfoDto(
                        stageId = it.id,
                        stageName = it.name,
                        type = it.type,
                        status = it.status,
                        isMaintaining = it.maintainer.any { m -> m.studentId == studentId },
                )
            }
        )

    fun mapCreateOfficialEvent(
        stage: Stage,
        dto: CreateOfficialStageDto
    ) = CreateStageOfficialEvent(
        id = UUID.randomUUID().toString(),
        stageId = stage.id,
        miniGame = OfficialStageMiniGameDto(
            coinToss = OfficialStageMiniGameInfoDto(
                isActive = dto.miniGame.coinToss.isActive,
                maxBettingPoint = dto.miniGame.coinToss.maxBettingPoint,
                minBettingPoint = dto.miniGame.coinToss.minBettingPoint,
                initialTicketCount = dto.miniGame.coinToss.initialTicketCount,
            ),
            yavarwee = OfficialStageMiniGameInfoDto(
                isActive = dto.miniGame.yavarwee.isActive,
                maxBettingPoint = dto.miniGame.yavarwee.maxBettingPoint,
                minBettingPoint = dto.miniGame.yavarwee.minBettingPoint,
                initialTicketCount = dto.miniGame.yavarwee.initialTicketCount,
            ),
            plinko = OfficialStageMiniGameInfoDto(
                isActive = dto.miniGame.plinko.isActive,
                maxBettingPoint = dto.miniGame.plinko.maxBettingPoint,
                minBettingPoint = dto.miniGame.plinko.minBettingPoint,
                initialTicketCount = dto.miniGame.plinko.initialTicketCount,
            )
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
