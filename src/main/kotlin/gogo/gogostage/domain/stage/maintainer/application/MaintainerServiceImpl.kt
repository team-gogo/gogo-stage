package gogo.gogostage.domain.stage.maintainer.application

import gogo.gogostage.domain.match.root.persistence.MatchRepository
import gogo.gogostage.domain.stage.maintainer.application.dto.IsMaintainerDto
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainerRepository
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class MaintainerServiceImpl(
    private val matchRepository: MatchRepository,
    private val maintainerRepository: StageMaintainerRepository,
) : MaintainerService {

    override fun isMaintainer(matchId: Long, studentId: Long): IsMaintainerDto {
        val match = (matchRepository.findNotEndMatchById(matchId)
            ?: throw StageException("Not Found Match, Match Id: $matchId", HttpStatus.NOT_FOUND.value()))
        val stage = match.game.stage

        val isMaintainer = maintainerRepository.existsByStageIdAndStudentId(stage.id, studentId)
        return IsMaintainerDto(isMaintainer)
    }

}
