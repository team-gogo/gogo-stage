package gogo.gogostage.domain.stage.root.event

data class CreateStageFastEvent(
    val id: String,
    val stageId: Long,
    val miniGame: FastStageMiniGameDto
)

data class FastStageMiniGameDto(
    val isCoinTossActive: Boolean,
)
