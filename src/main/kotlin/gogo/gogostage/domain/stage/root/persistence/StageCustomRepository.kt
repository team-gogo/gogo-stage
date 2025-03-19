package gogo.gogostage.domain.stage.root.persistence

import gogo.gogostage.domain.stage.root.application.dto.StageParticipantPointRankDto
import org.springframework.data.domain.Pageable

interface StageCustomRepository {
    fun queryPointRank(stage: Stage, pageable: Pageable, size: Int): StageParticipantPointRankDto
}