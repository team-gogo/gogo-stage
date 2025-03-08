package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardReqDto
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardResDto
import gogo.gogostage.domain.community.root.application.dto.WriteCommunityBoardDto
import org.springframework.data.domain.Page

interface CommunityService {
    fun writeCommunityBoard(stageId: Long, writeCommunityBoardDto: WriteCommunityBoardDto)
    fun getStageBoard(stageId: Long, getCommunityBoardDto: GetCommunityBoardReqDto): Page<GetCommunityBoardResDto>
}