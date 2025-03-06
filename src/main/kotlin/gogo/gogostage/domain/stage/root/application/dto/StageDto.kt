package gogo.gogostage.domain.stage.root.application.dto

import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.game.persistence.GameSystem

data class CreateFastStageDto(
    val stageName: String,
    val game: CreateStageGameDto,
    val initialPoint: Long,
    val rule: CreateStageRuleDto,
    val miniGame: CreateFastStageMiniGameDto,
    val passCode: String?,
    val maintainer: List<Long>
)

data class CreateStageGameDto(
    val category: GameCategory,
    val name: String,
    val system: GameSystem,
    val teamMinCapacity: Int,
    val teamMaxCapacity: Int,
)

data class CreateStageRuleDto(
    val maxBettingPoint: Long,
    val minBettingPoint: Long,
)

data class CreateFastStageMiniGameDto(
    val coinToss: MiniGameInfoDto
)

data class MiniGameInfoDto(
    val isActive: Boolean,
    val maxBettingPoint: Long?,
)
