package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import gogo.gogostage.domain.match.root.application.dto.StageApiInfoDto
import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainerRepository
import org.springframework.stereotype.Component

@Component
class MatchMapper(
    private val maintainerRepository: StageMaintainerRepository
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

}
