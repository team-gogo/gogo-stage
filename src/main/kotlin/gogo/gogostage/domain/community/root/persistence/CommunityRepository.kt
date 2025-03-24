package gogo.gogostage.domain.community.root.persistence

import gogo.gogostage.domain.game.persistence.GameCategory
import org.springframework.data.jpa.repository.JpaRepository

interface CommunityRepository : JpaRepository<Community, Long>, CommunityCustomRepository {
    fun findByStageIdAndCategory(stageId: Long, gameCategory: GameCategory): Community?
}
