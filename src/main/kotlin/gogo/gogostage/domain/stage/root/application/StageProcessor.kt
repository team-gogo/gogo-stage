package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.community.root.persistence.Community
import gogo.gogostage.domain.community.root.persistence.CommunityRepository
import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.game.persistence.GameRepository
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainer
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainerRepository
import gogo.gogostage.domain.stage.minigameinfo.persistence.MiniGameInfo
import gogo.gogostage.domain.stage.minigameinfo.persistence.MiniGameInfoRepository
import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.persistence.Stage
import gogo.gogostage.domain.stage.root.persistence.StageRepository
import gogo.gogostage.domain.stage.rule.persistence.StageRule
import gogo.gogostage.domain.stage.rule.persistence.StageRuleRepository
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.stereotype.Component

@Component
class StageProcessor(
    private val stageRepository: StageRepository,
    private val miniGameInfoRepository: MiniGameInfoRepository,
    private val stageRuleRepository: StageRuleRepository,
    private val stageMaintainerRepository: StageMaintainerRepository,
    private val gameRepository: GameRepository,
    private val communityRepository: CommunityRepository,
) {

    fun saveFast(student: StudentByIdStub, dto: CreateFastStageDto): Stage {
        val isActiveCoinToss = dto.miniGame.coinToss.isActive

        val stage = Stage.fastOf(student, dto, isActiveCoinToss)
        stageRepository.save(stage)

        val miniGameInfo = MiniGameInfo.ofFast(stage, isActiveCoinToss)
        miniGameInfoRepository.save(miniGameInfo)

        val stageRule = StageRule.of(stage, dto.rule)
        stageRuleRepository.save(stageRule)

        val maintainers =
            dto.maintainer.map { StageMaintainer.of(stage, it) } + StageMaintainer.of(stage, student.studentId)
        stageMaintainerRepository.saveAll(maintainers)

        val gameDto = dto.game
        val game = Game.of(stage, gameDto.category, gameDto.name, gameDto.system)
        gameRepository.save(game)

        val community = Community.of(stage, game.category)
        communityRepository.save(community)

        return stage
    }

}
