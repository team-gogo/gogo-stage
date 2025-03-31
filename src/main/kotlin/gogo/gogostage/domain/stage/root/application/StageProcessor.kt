package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.community.root.persistence.Community
import gogo.gogostage.domain.community.root.persistence.CommunityRepository
import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.game.persistence.GameRepository
import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.match.root.persistence.MatchRepository
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainer
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainerRepository
import gogo.gogostage.domain.stage.minigameinfo.persistence.MiniGameInfo
import gogo.gogostage.domain.stage.minigameinfo.persistence.MiniGameInfoRepository
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipant
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import gogo.gogostage.domain.stage.root.application.dto.StageConfirmDto
import gogo.gogostage.domain.stage.root.persistence.Stage
import gogo.gogostage.domain.stage.root.persistence.StageRepository
import gogo.gogostage.domain.stage.root.persistence.StageType
import gogo.gogostage.domain.stage.rule.persistence.StageRule
import gogo.gogostage.domain.stage.rule.persistence.StageRuleRepository
import gogo.gogostage.domain.team.root.persistence.TeamRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class StageProcessor(
    private val stageRepository: StageRepository,
    private val miniGameInfoRepository: MiniGameInfoRepository,
    private val stageRuleRepository: StageRuleRepository,
    private val stageMaintainerRepository: StageMaintainerRepository,
    private val stageParticipantRepository: StageParticipantRepository,
    private val gameRepository: GameRepository,
    private val communityRepository: CommunityRepository,
    private val teamRepository: TeamRepository,
    private val matchRepository: MatchRepository,
) {

    fun saveFast(student: StudentByIdStub, dto: CreateFastStageDto): Stage {
        val isActiveCoinToss = dto.miniGame.coinToss.isActive

        val stage = Stage.fastOf(student, dto, isActiveCoinToss)
        stageRepository.save(stage)

        val miniGameInfo = MiniGameInfo.fastOf(stage, isActiveCoinToss)
        miniGameInfoRepository.save(miniGameInfo)

        val stageRule = StageRule.of(stage, dto.rule)
        stageRuleRepository.save(stageRule)

        if (dto.maintainer.isNotEmpty()) {
            val maintainers =
                dto.maintainer.toSet().toList()
                    .filter { student.studentId != it }
                    .map { StageMaintainer.of(stage, it) } +
                        StageMaintainer.of(stage, student.studentId)
            stageMaintainerRepository.saveAll(maintainers)
        } else {
            val maintainer = StageMaintainer.of(stage, student.studentId)
            stageMaintainerRepository.save(maintainer)
        }

        val gameDto = dto.game
        val game = Game.of(stage, gameDto.category, gameDto.name, gameDto.system, gameDto.teamMinCapacity, gameDto.teamMaxCapacity)
        gameRepository.save(game)

        val community = Community.of(stage, game.category)
        communityRepository.save(community)

        return stage
    }

    fun saveOfficial(student: StudentByIdStub, dto: CreateOfficialStageDto): Stage {
        val isActiveMiniGame =
            dto.miniGame.coinToss.isActive || dto.miniGame.yavarwee.isActive || dto.miniGame.plinko.isActive
        val isActiveShop =
            dto.shop.coinToss.isActive || dto.shop.yavarwee.isActive || dto.shop.plinko.isActive

        val stage = Stage.officialOf(student, dto, isActiveMiniGame, isActiveShop)
        stageRepository.save(stage)

        val miniGameInfo = MiniGameInfo.officialOf(stage, dto.miniGame)
        miniGameInfoRepository.save(miniGameInfo)

        val stageRule = StageRule.of(stage, dto.rule)
        stageRuleRepository.save(stageRule)

        if (dto.maintainer.isNotEmpty()) {
            val maintainers =
                dto.maintainer.toSet().toList()
                    .filter { student.studentId != it }
                    .map { StageMaintainer.of(stage, it) } +
                        StageMaintainer.of(stage, student.studentId)

            stageMaintainerRepository.saveAll(maintainers)
        } else {
            val maintainer = StageMaintainer.of(stage, student.studentId)
            stageMaintainerRepository.save(maintainer)
        }

        val games = dto.game.map { Game.of(stage, it.category, it.name, it.system, it.teamMinCapacity, it.teamMaxCapacity) }
        gameRepository.saveAll(games)

        val gameCategories = games.map { it.category }.toSet().toList()
        val communities = gameCategories.map { Community.of(stage, it) }
        communityRepository.saveAll(communities)

        return stage
    }

    fun join(student: StudentByIdStub, stage: Stage) {
        val stageParticipant = StageParticipant.of(stage, student.studentId, stage.initialPoint)
        stageParticipantRepository.save(stageParticipant)
    }

    fun confirm(stage: Stage, dto: StageConfirmDto) {
        val type = stage.type

        when (type) {
            StageType.FAST -> saveMatch(dto)
            StageType.OFFICIAL -> saveMatch(dto)
        }

        stage.confirm()
        stageRepository.save(stage)
    }

    private fun saveMatch(dto: StageConfirmDto) {
        dto.games.forEach { g ->

            val game = gameRepository.findByIdOrNull(g.gameId)
                ?: throw StageException("Game Not Found, Game Id = ${g.gameId}", HttpStatus.NOT_FOUND.value())

            when (game.system) {
                GameSystem.SINGLE -> {
                    val single = g.single!!

                    val teamA = teamRepository.findByIdOrNull(single.teamAId)
                        ?: throw StageException("Team Not Found, Team Id = ${single.teamAId}", HttpStatus.NOT_FOUND.value())
                    val teamB = (teamRepository.findByIdOrNull(single.teamBId)
                        ?: throw StageException("Team Not Found, Team Id = ${single.teamBId}", HttpStatus.NOT_FOUND.value()))

                    teamA.participation()
                    teamB.participation()
                    teamRepository.save(teamA)
                    teamRepository.save(teamB)

                    val match = Match.singleOf(
                        game = game,
                        aTeam = teamA,
                        bTeam = teamB,
                        startDate = single.startDate,
                        endDate = single.endDate
                    )
                    matchRepository.save(match)
                }
                GameSystem.FULL_LEAGUE -> {
                    val fullLeague = g.fullLeague!!
                    var teamCount = 0

                    val matches = fullLeague.map {
                        val teamA = teamRepository.findByIdOrNull(it.teamAId)
                            ?: throw StageException("Team Not Found, Team Id = ${it.teamAId}", HttpStatus.NOT_FOUND.value())
                        val teamB = (teamRepository.findByIdOrNull(it.teamBId)
                            ?: throw StageException("Team Not Found, Team Id = ${it.teamBId}", HttpStatus.NOT_FOUND.value()))

                        teamCount += teamA.participation()
                        teamCount += teamB.participation()
                        teamRepository.save(teamA)
                        teamRepository.save(teamB)

                        Match.leagueOf(
                            game = game,
                            aTeam = teamA,
                            bTeam = teamB,
                            startDate = it.startDate,
                            endDate = it.endDate,
                            leagueTurn = it.leagueTurn
                        )
                    }

                    val matchCount = (teamCount * (teamCount - 1)) / 2
                    if (matchCount != fullLeague.size) {
                        throw StageException("풀 리그 경기의 매치의 수가 맞지 않습니다. 에상 = ${matchCount}, 실제 = ${fullLeague.size}", HttpStatus.BAD_REQUEST.value())
                    }

                    game.updateLeagueCount(matchCount)
                    gameRepository.save(game)

                    matchRepository.saveAll(matches)
                }
                GameSystem.TOURNAMENT -> {
                    val tournament = g.tournament!!
                    var teamCount = 0

                    val matches = tournament.map {
                        val teamA = if (it.teamAId == null) null else teamRepository.findByIdOrNull(it.teamAId)
                            ?: throw StageException("Team Not Found, Team Id = ${it.teamAId}", HttpStatus.NOT_FOUND.value())
                        val teamB = if (it.teamBId == null) null else teamRepository.findByIdOrNull(it.teamBId)
                            ?: throw StageException("Team Not Found, Team Id = ${it.teamBId}", HttpStatus.NOT_FOUND.value())

                        if (teamA != null) {
                            teamCount += teamA.participation()
                            teamRepository.save(teamA)
                        }
                        if (teamB != null) {
                            teamCount += teamB.participation()
                            teamRepository.save(teamB)
                        }

                        Match.tournamentOf(
                            game = game,
                            aTeam = teamA,
                            bTeam = teamB,
                            startDate = it.startDate,
                            endDate = it.endDate,
                            round = it.round,
                            turn = it.turn,
                        )
                    }

                    val matchCount = teamCount - 1
                    if (matchCount != tournament.size) {
                        throw StageException("토너먼트 경기의 매치의 수가 맞지 않습니다. 에상 = ${matchCount}, 실제 = ${tournament.size}", HttpStatus.BAD_REQUEST.value())
                    }

                    matchRepository.saveAll(matches)
                }
            }

        }
    }

    private fun factorial(n: Int): Int {
        var result = 1
        for (i in 2..n) {
            result *= i
        }
        return result
    }

}
