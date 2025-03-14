package gogo.gogostage.domain.community.root.persistence

import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardInfoResDto
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardResDto
import gogo.gogostage.domain.game.persistence.GameCategory
import org.springframework.data.domain.Pageable

interface CommunityCustomRepository {
    fun searchCommunityBoardPage(stageId: Long, size: Int, category: GameCategory?, sort: SortType, pageable: Pageable): GetCommunityBoardResDto
    fun getCommunityBoardInfo(boardId: Long): GetCommunityBoardInfoResDto
}