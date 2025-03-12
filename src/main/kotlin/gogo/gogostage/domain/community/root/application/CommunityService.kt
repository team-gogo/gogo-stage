package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardInfoResDto
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardResDto
import gogo.gogostage.domain.community.root.application.dto.WriteCommunityBoardDto
import gogo.gogostage.domain.community.root.persistence.SortType
import gogo.gogostage.domain.game.persistence.GameCategory

interface CommunityService {
    fun writeCommunityBoard(stageId: Long, writeCommunityBoardDto: WriteCommunityBoardDto)
    fun getStageBoard(stageId: Long, page: Int, size: Int, category: GameCategory?, sort: SortType): GetCommunityBoardResDto
    fun getStageBoardInfo(boardId: Long): GetCommunityBoardInfoResDto
}