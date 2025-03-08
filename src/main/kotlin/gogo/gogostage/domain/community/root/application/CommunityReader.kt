package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardReqDto
import gogo.gogostage.domain.community.root.persistence.Community
import gogo.gogostage.domain.community.root.persistence.CommunityRepository
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CommunityReader(
    private val communityRepository: CommunityRepository
) {

    fun readByStageId(stageId: Long) =
        communityRepository.findByStageId(stageId)
            ?: throw StageException("Stage Not Found, stageId=$stageId", HttpStatus.NOT_FOUND.value())

    fun readBoards(community: Community, getCommunityBoardReqDto: GetCommunityBoardReqDto) =
        communityRepository.searchCommunityBoardPage(community, getCommunityBoardReqDto)
}