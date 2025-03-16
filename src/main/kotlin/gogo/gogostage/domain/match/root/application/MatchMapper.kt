package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.*
import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainerRepository
import gogo.gogostage.global.internal.betting.api.BettingApi
import org.springframework.stereotype.Component

@Component
class MatchMapper(
    private val maintainerRepository: StageMaintainerRepository,
    private val bettingApi: BettingApi
) {

    fun mapInfo(match: Match): MatchApiInfoDto {

        val stage = match.game.stage
        val maintainers = maintainerRepository.findByStage(stage)

        return MatchApiInfoDto(
            startDate = match.startDate,
            endDate = match.endDate,
            stage = StageApiInfoDto(
                maintainers = maintainers.map { it.studentId }
            )
        )
    }

    fun mapSearch(matches: List<Match>, studentId: Long): MatchSearchDto {
        val matchIds = matches.map { it.id }
        val bundleDto = bettingApi.bundle(matchIds, studentId)

        val matchInfoList = matches.map { matchEntity ->
            val bettingInfo = bundleDto.bettings.find { it.matchId == matchEntity.id }
            val resultInfo = bettingInfo?.result?.let {
                MatchResultInfoDto(
                    victoryTeamId = matchEntity.matchResult!!.victoryTeam.id,
                    aTeamScore = matchEntity.matchResult!!.aTeamScore,
                    bTeamScore = matchEntity.matchResult!!.bTeamScore,
                    isPredictionSuccess = it.isPredicted,
                    earnedPoint = it.earnedPoint
                )
            } ?: matchEntity.matchResult?.let {
                MatchResultInfoDto(
                    victoryTeamId = it.victoryTeam.id,
                    aTeamScore = it.aTeamScore,
                    bTeamScore = it.bTeamScore,
                    isPredictionSuccess = null,
                    earnedPoint = null
                )
            }

            MatchInfoDto(
                matchId = matchEntity.id,
                aTeam = MatchTeamInfoDto(
                    teamId = matchEntity.aTeam?.id,
                    teamName = if (matchEntity.aTeam != null) matchEntity.aTeam!!.name else "TBD",
                    bettingPoint = matchEntity.aTeamBettingPoint,
                    winCount = matchEntity.aTeam?.winCount
                ),
                bTeam = MatchTeamInfoDto(
                    teamId = matchEntity.bTeam?.id,
                    teamName = if (matchEntity.bTeam != null) matchEntity.bTeam!!.name else "TBD",
                    bettingPoint = matchEntity.bTeamBettingPoint,
                    winCount = matchEntity.bTeam?.winCount
                ),
                startDate = matchEntity.startDate,
                endDate = matchEntity.endDate,
                isEnd = matchEntity.isEnd,
                round = matchEntity.round,
                category = matchEntity.game.category,
                gameName = matchEntity.game.name,
                system = matchEntity.game.system,
                turn = matchEntity.turn,
                betting = bettingInfo?.let {
                    MatchBettingInfoDto(
                        isBetting = true,
                        bettingPoint = it.betting.bettingPoint,
                        predictedWinTeamId = it.betting.predictedWinTeamId
                    )
                } ?: MatchBettingInfoDto(
                    isBetting = false,
                    bettingPoint = null,
                    predictedWinTeamId = null
                ),
                result = resultInfo
            )
        }

        return MatchSearchDto(count = matchInfoList.size, matches = matchInfoList)
    }

}
