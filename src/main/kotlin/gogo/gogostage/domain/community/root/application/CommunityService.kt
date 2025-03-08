package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.root.application.dto.WriteCommunityBoardDto

interface CommunityService {
    fun writeCommunityBoard(stageId: Long, writeCommunityBoardDto: WriteCommunityBoardDto)
}