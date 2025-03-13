package gogo.gogostage.domain.stage.root.event

data class CreateStageOfficialEvent(
    val id: String,
    val stageId: Long,
    val miniGame: OfficialStageMiniGameDto,
    val shop: OfficialStageShopDto
)

data class OfficialStageMiniGameDto(
    val coinToss: OfficialStageMiniGameInfoDto,
    val yavarwee: OfficialStageMiniGameInfoDto,
    val plinko: OfficialStageMiniGameInfoDto,
)

data class OfficialStageShopDto(
    val coinToss: OfficialStageShopInfoDto,
    val yavarwee: OfficialStageShopInfoDto,
    val plinko: OfficialStageShopInfoDto,
)

data class OfficialStageShopInfoDto(
    val isActive: Boolean,
    val price: Long?,
    val quantity: Int?,
)
data class OfficialStageMiniGameInfoDto(
    val isActive: Boolean,
    val maxBettingPoint: Long?,
    val minBettingPoint: Long?,
)
