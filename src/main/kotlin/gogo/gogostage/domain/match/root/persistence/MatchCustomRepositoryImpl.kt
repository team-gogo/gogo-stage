package gogo.gogostage.domain.match.root.persistence

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPQLQueryFactory
import gogo.gogostage.domain.match.root.application.dto.*
import gogo.gogostage.domain.match.root.persistence.QMatch.*
import gogo.gogostage.domain.stage.root.persistence.QStage.stage
import gogo.gogostage.global.internal.betting.api.BettingApi
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class MatchCustomRepositoryImpl(
    private val queryFactory: JPQLQueryFactory,
    private val bettingApi: BettingApi,

    ) : MatchCustomRepository {

    override fun search(stageId: Long, studentId: Long, y: Int, m: Int, d: Int): MatchSearchDto {
        val matches = queryFactory.select(
            Projections.constructor(
                MatchInfoDto::class.java,
                match.id,
                Projections.constructor(
                    MatchTeamInfoDto::class.java,
                    match.aTeam.id,
                    match.aTeam.name,
                    match.aTeamBettingPoint,
                    match.aTeam.winCount
                ),
                Projections.constructor(
                    MatchTeamInfoDto::class.java,
                    match.bTeam.id,
                    match.bTeam.name,
                    match.bTeamBettingPoint,
                    match.bTeam.winCount
                ),
                match.startDate,
                match.endDate,
                match.isEnd,
                match.round,
                match.game.category,
                match.game.name,
                match.game.system,
                match.turn,
                null, // betting 필드 (이후 업데이트)
                Projections.constructor(
                    MatchResultInfoDto::class.java,
                    match.matchResult.victoryTeam.id,
                    match.matchResult.aTeamScore,
                    match.matchResult.bTeamScore,
                    null,
                    null
                )
            )
        )
        .from(match)
        .where(
            match.game.stage.id.eq(stage.id)
                .and(
                    Expressions.dateTemplate(
                        LocalDate::class.java, "DATE({0})", match.startDate
                    ).eq(LocalDate.of(y, m, d))
                )
        ).fetch()

        val matchIds = matches.map { it.matchId }
        val bundleDto = bettingApi.bundle(matchIds, studentId)

        val matchMap = matches.associateBy { it.matchId }.toMutableMap()

        bundleDto.bettings.forEach { bettingInfo ->
            matchMap[bettingInfo.matchId]?.let { matchInfo ->
                matchMap[bettingInfo.matchId] = matchInfo.copy(
                    betting = MatchBettingInfoDto(
                        isBetting = true,
                        bettingPoint = bettingInfo.betting.bettingPoint,
                        predictedWinTeamId = bettingInfo.betting.predictedWinTeamId
                    ),
                    result = bettingInfo.result?.let {
                        MatchResultInfoDto(
                            victoryTeamId = matchInfo.result!!.victoryTeamId,
                            aTeamScore = matchInfo.result!!.aTeamScore,
                            bTeamScore = matchInfo.result!!.bTeamScore,
                            isPredictionSuccess = it.isPredicted,
                            earnedPoint = it.earnedPoint
                        )
                    } ?: matchInfo.result
                )
            }
        }

        val updatedMatches = matchMap.values.toList()

        return MatchSearchDto(count = updatedMatches.size, matches = updatedMatches)
    }

}
