package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.board.application.BoardReader
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardInfoResDto
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardResDto

import gogo.gogostage.domain.community.root.persistence.CommunityRepository
import gogo.gogostage.domain.community.root.persistence.SortType
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.global.error.StageException
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CommunityReader(
    private val communityRepository: CommunityRepository,
    private val boardReader: BoardReader
) {

    fun readByStageId(stageId: Long) =
        communityRepository.findByStageId(stageId)
            ?: throw StageException("Stage Not Found, stageId=$stageId", HttpStatus.NOT_FOUND.value())

    fun readBoards(stageId: Long, page: Int, size: Int, category: GameCategory?, sort: SortType): GetCommunityBoardResDto {
        val pageRequest = PageRequest.of(page, size)
        return communityRepository.searchCommunityBoardPage(stageId, size, category, sort, pageRequest)
    }

    fun readBoardInfo(boardId: Long): GetCommunityBoardInfoResDto =
        communityRepository.getCommunityBoardInfo(boardId)

    fun readBoardByBoardId(boardId: Long) =
        boardReader.read(boardId)

}