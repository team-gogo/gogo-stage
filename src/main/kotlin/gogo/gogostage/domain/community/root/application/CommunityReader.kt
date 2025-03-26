package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.board.application.BoardReader
import gogo.gogostage.domain.community.comment.application.CommentReader
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardInfoResDto
import gogo.gogostage.domain.community.root.application.dto.GetCommunityBoardResDto

import gogo.gogostage.domain.community.root.persistence.CommunityRepository
import gogo.gogostage.domain.community.root.persistence.SortType
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CommunityReader(
    private val communityRepository: CommunityRepository,
    private val boardReader: BoardReader,
    private val commentReader: CommentReader
) {

    fun readByStageIdAndGameCategory(stageId: Long, gameCategory: GameCategory) =
        communityRepository.findByStageIdAndCategory(stageId, gameCategory)
            ?: throw StageException("Community Not Found, stageId=$stageId gameCategory=$gameCategory", HttpStatus.NOT_FOUND.value())

    fun readBoards(stageId: Long, page: Int, size: Int, category: GameCategory?, sort: SortType): GetCommunityBoardResDto {
        val pageRequest = PageRequest.of(page, size)
        return communityRepository.searchCommunityBoardPage(stageId, size, category, sort, pageRequest)
    }

    fun readBoardInfo(boardId: Long, student: StudentByIdStub): GetCommunityBoardInfoResDto =
        communityRepository.getCommunityBoardInfo(boardId, student)

    fun readBoardByBoardId(boardId: Long) =
        boardReader.read(boardId)

    fun readCommentByCommentId(commentId: Long) =
        commentReader.read(commentId)

}