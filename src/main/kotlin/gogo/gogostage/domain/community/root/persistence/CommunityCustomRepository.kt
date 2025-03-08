package gogo.gogostage.domain.community.root.persistence

import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardReqDto
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardResDto
import org.springframework.data.domain.Page

interface CommunityCustomRepository {
    fun searchCommunityBoardPage(community: Community, getCommunityBoardReqDto: GetCommunityBoardReqDto): Page<GetCommunityBoardResDto>
}