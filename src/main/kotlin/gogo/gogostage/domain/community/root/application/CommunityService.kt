package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.root.application.dto.WriteCommunityBoardDto

interface CommunityService {
    fun writeCommunityBoard(gameId: Long, writeCommunityBoardDto: WriteCommunityBoardDto)
}