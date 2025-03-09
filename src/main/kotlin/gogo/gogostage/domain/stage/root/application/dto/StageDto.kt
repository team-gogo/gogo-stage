package gogo.gogostage.domain.stage.root.application.dto

import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.domain.match.root.persistence.Round
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateFastStageDto(
    @NotBlank
    val stageName: String,
    @NotNull
    val game: CreateStageGameDto,
    @NotNull
    @Size(min = 1000, max = 100000)
    val initialPoint: Long,
    @NotNull
    val rule: CreateStageRuleDto,
    @NotNull
    val miniGame: CreateFastStageMiniGameDto,
    val passCode: String?,
    @NotNull
    val maintainer: List<Long>
)

data class CreateOfficialStageDto(
    @NotBlank
    val stageName: String,
    @NotNull
    val game: List<CreateStageGameDto>,
    @NotNull
    @Size(min = 1000, max = 100000)
    val initialPoint: Long,
    @NotNull
    val rule: CreateStageRuleDto,
    @NotNull
    val miniGame: CreateOfficialStageMiniGameDto,
    @NotNull
    val shop: CreateStageShopDto,
    val passCode: String?,
    @NotNull
    val maintainer: List<Long>
)

data class CreateStageGameDto(
    @NotNull
    val category: GameCategory,
    val name: String,
    @NotNull
    val system: GameSystem,
    @NotNull
    val teamMinCapacity: Int,
    @NotNull
    val teamMaxCapacity: Int,
)

data class CreateStageRuleDto(
    @NotNull
    val maxBettingPoint: Long,
    @NotNull
    val minBettingPoint: Long,
)

data class CreateStageShopDto(
    @NotNull
    val coinToss: ShopInfoDto,
    @NotNull
    val yavarwee: ShopInfoDto,
    @NotNull
    val plinko: ShopInfoDto,
)

data class CreateFastStageMiniGameDto(
    @NotNull
    val coinToss: MiniGameInfoDto
)

data class CreateOfficialStageMiniGameDto(
    @NotNull
    val coinToss: MiniGameInfoDto,
    @NotNull
    val yavarwee: MiniGameInfoDto,
    @NotNull
    val plinko: MiniGameInfoDto
)

data class ShopInfoDto(
    @NotNull
    val isActive: Boolean,
    val price: Long?,
    val quantity: Int?,
)

data class MiniGameInfoDto(
    @NotNull
    val isActive: Boolean,
    val maxBettingPoint: Long?,
)

data class StageConfirmDto(
    val game: List<StageConfirmGamInfoDto>
)

data class StageConfirmGamInfoDto(
    val gameId: Long,
    val single: StageConfirmSingleSystemInfoDto,
    val tournament: StageConfirmTournamentSystemInfoDto,
    val fullLeague: StageConfirmFullLeagueSystemInfoDto
)

data class StageConfirmSingleSystemInfoDto(
    val aTeam: Long,
    val bTeam: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)

data class StageConfirmTournamentSystemInfoDto(
    val aTeam: Long?,
    val bTeam: Long?,
    val round: Round,
    val turn: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)

data class StageConfirmFullLeagueSystemInfoDto(
    val aTeam: Long,
    val bTeam: Long,
    val leagueTurn: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)
