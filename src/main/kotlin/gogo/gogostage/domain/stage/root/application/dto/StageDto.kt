package gogo.gogostage.domain.stage.root.application.dto

import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.domain.match.root.persistence.Round
import gogo.gogostage.domain.stage.root.persistence.StageStatus
import gogo.gogostage.domain.stage.root.persistence.StageType
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
    val minBettingPoint: Long?,
    val initialTicketCount: Int?
)

data class StageJoinDto(
    val passCode: String?,
)

data class StageConfirmDto(
    val games: List<StageConfirmGameDto>
)

data class StageConfirmGameDto(
    val gameId: Long,
    val single: StageConfirmGameSingleDto?,
    val tournament: List<StageConfirmGameTournamentDto>?,
    val fullLeague: List<StageConfirmGameFullLeagueDto>?,
)

data class StageConfirmGameSingleDto(
    val teamAId: Long,
    val teamBId: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)

data class StageConfirmGameTournamentDto(
    val teamAId: Long?,
    val teamBId: Long?,
    val round: Round,
    val turn: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)

data class StageConfirmGameFullLeagueDto(
    val teamAId: Long,
    val teamBId: Long,
    val leagueTurn: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)

data class QueryStageDto(
    val count: Int,
    val stages: List<QueryStageInfoDto>
)

data class QueryStageInfoDto(
    val stageId: Long,
    val stageName: String,
    val type: StageType,
    val status: StageStatus,
    val participantCount: Int,
    val isParticipating: Boolean,
    val isMaintainer: Boolean,
    val isPassCode: Boolean,
)

data class QueryMyStageDto(
    val stages: List<QueryMyStageInfoDto>
)

data class QueryMyStageInfoDto(
    val stageId: Long,
    val stageName: String,
    val type: StageType,
    val status: StageStatus,
    val isMaintaining: Boolean
)

data class StageParticipantPointRankDto(
    val info: PageDto,
    val rank: List<PointRankDto>
)

data class PageDto(
    val totalPage: Int,
    val totalElement: Int
)

data class PointRankDto(
    val rank: Int,
    val studentId: Long,
    val point: Long,
    val name: String,
    val classNumber: Int,
    val studentNumber: Int
)

data class CheckStageMaintainerDto(
    val isMaintainer: Boolean
)

data class QueryMyWastedDto(
    val isWasted: Boolean
)
