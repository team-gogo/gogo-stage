package gogo.gogostage.domain.stage.root.event

data class CreateStageOfficialEvent(
    val id: String,
    val stageId: Long,
    val miniGame: OfficialStageMiniGameDto,
    val shop: OfficialStageShopDto
)

data class OfficialStageMiniGameDto(
    val isCoinTossActive: Boolean,
    val isYavarweeActive: Boolean,
    val isPlinkoActive: Boolean,
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
