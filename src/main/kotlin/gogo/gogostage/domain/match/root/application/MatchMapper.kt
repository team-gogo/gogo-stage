package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.*
import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainerRepository
import gogo.gogostage.global.internal.betting.api.BettingApi
import gogo.gogostage.global.internal.betting.stub.BettingBundleDto
import gogo.gogostage.global.internal.student.api.StudentApi
import org.springframework.stereotype.Component

@Component
class MatchMapper(
    private val maintainerRepository: StageMaintainerRepository,
    private val bettingApi: BettingApi,
    private val studentApi: StudentApi,
) {

    fun mapApiInfo(match: Match): MatchApiInfoDto {

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

            MatchSearchInfoDto(
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

    fun mapMy(bettingBundle: BettingBundleDto, matches: List<Match>): MatchSearchDto {
        val bettingMap = bettingBundle.bettings.associateBy { it.matchId }

        val matchSearchInfoList = matches.map { match ->
            val bettingInfo = bettingMap[match.id]
            val bettingDto = bettingInfo?.let {
                MatchBettingInfoDto(
                    isBetting = true,
                    bettingPoint = it.betting.bettingPoint,
                    predictedWinTeamId = it.betting.predictedWinTeamId
                )
            }

            val resultDto = bettingInfo?.result?.let {
                MatchResultInfoDto(
                    victoryTeamId = match.matchResult!!.victoryTeam.id,
                    aTeamScore = match.matchResult!!.aTeamScore,
                    bTeamScore = match.matchResult!!.bTeamScore,
                    isPredictionSuccess = it.isPredicted,
                    earnedPoint = it.earnedPoint
                )
            }

            MatchSearchInfoDto(
                matchId = match.id,
                aTeam = MatchTeamInfoDto(
                    teamId = match.aTeam?.id,
                    teamName = match.aTeam?.name ?: "TBD",
                    bettingPoint = match.aTeamBettingPoint,
                    winCount = match.aTeam?.winCount
                ),
                bTeam = MatchTeamInfoDto(
                    teamId = match.bTeam?.id,
                    teamName = match.bTeam?.name ?: "TBD",
                    bettingPoint = match.bTeamBettingPoint,
                    winCount = match.bTeam?.winCount
                ),
                startDate = match.startDate,
                endDate = match.endDate,
                isEnd = match.isEnd,
                round = match.round,
                category = match.game.category,
                gameName = match.game.name,
                system = match.game.system,
                turn = match.turn,
                betting = bettingDto,
                result = resultDto
            )
        }

        return MatchSearchDto(
            count = matchSearchInfoList.size,
            matches = matchSearchInfoList
        )
    }

    fun mapInfo(match: Match): MatchInfoDto {
        val aTeamStudents = studentApi.queryByStudentsIds(match.aTeam?.participants!!.map { it.studentId })
        val bTeamStudents = studentApi.queryByStudentsIds(match.bTeam?.participants!!.map { it.studentId })

        val aTeamDto = match.aTeam?.participants!!.map { match ->
            val studentDto = aTeamStudents.students.find { it.studentId == match.studentId }
            MatchTeamParticipantInfoDto(
                studentId = match.studentId,
                name = studentDto!!.name,
                classNumber = studentDto.classNumber,
                studentNumber = studentDto.classNumber,
                positionX = match.positionX,
                positionY = match.positionY
            )
        }

        val bTeamDto = match.bTeam?.participants!!.map { match ->
            val studentDto = bTeamStudents.students.find { it.studentId == match.studentId }
            MatchTeamParticipantInfoDto(
                studentId = match.studentId,
                name = studentDto!!.name,
                classNumber = studentDto.classNumber,
                studentNumber = studentDto.classNumber,
                positionX = match.positionX,
                positionY = match.positionY
            )
        }

        return MatchInfoDto(
            matchId = match.id,
            aTeam = MatchTeamInfoDto(
                teamId = match.aTeam?.id,
                teamName = if (match.aTeam != null) match.aTeam!!.name else "TBD",
                winCount = match.aTeam?.winCount,
                bettingPoint = match.aTeamBettingPoint,
                participants = aTeamDto
            ),
            bTeam = MatchTeamInfoDto(
                teamId = match.bTeam?.id,
                teamName = if (match.bTeam != null) match.bTeam!!.name else "TBD",
                winCount = match.bTeam?.winCount,
                bettingPoint = match.bTeamBettingPoint,
                participants = bTeamDto
            ),
            startDate = match.startDate,
            endDate = match.endDate,
            isEnd = match.isEnd,
            round = match.round,
            category = match.game.category,
            system = match.game.system,
            gameName = match.game.name,
            turn = match.turn,
        )

    }

}
