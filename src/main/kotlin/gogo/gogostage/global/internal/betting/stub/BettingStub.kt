package gogo.gogostage.global.internal.betting.stub

data class BettingBundleDto(
    val bettings: List<BettingBundleInfoDto>
)

data class BettingBundleInfoDto(
    val matchId: Long,
    val betting: BettingInfoDto,
    val result: BettingResultInfoDto?
)

data class BettingInfoDto(
    val bettingId: Long,
    val bettingPoint: Long,
    val predictedWinTeamId: Long,
)

data class BettingResultInfoDto(
    val isPredicted: Boolean,
    val earnedPoint: Long,
)
