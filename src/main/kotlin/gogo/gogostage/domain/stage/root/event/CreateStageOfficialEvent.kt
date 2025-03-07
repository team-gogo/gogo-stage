package gogo.gogostage.domain.stage.root.event

data class CreateStageOfficialEvent(
    val id: String,
    val stageId: Long,
    val miniGame: OfficialStageMiniGameInfoDto,
    val shop: OfficialStageMiniGameInfoDto
)

data class OfficialStageMiniGameInfoDto(
    val isCoinTossActive: Boolean,
    val isYavarweeActive: Boolean,
    val isPlinkoActive: Boolean,
)
