package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.board.application.BoardProcessor
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.community.root.persistence.SortType
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommunityServiceImpl(
    private val communityReader: CommunityReader,
    private val boardProcessor: BoardProcessor,
    private val userUtil: UserContextUtil,
    private val communityProcessor: CommunityProcessor,
    private val communityValidator: CommunityValidator
): CommunityService {

    @Transactional
    override fun writeCommunityBoard(stageId: Long, writeCommunityBoardDto: WriteCommunityBoardDto) {
        val student = userUtil.getCurrentStudent()
        val community = communityReader.readByStageIdAndGameCategory(stageId, writeCommunityBoardDto.gameCategory)
        boardProcessor.saveCommunityBoard(community, student.studentId, writeCommunityBoardDto)

        // 추후 욕설 필터링 요청 처리 필요
    }

    @Transactional(readOnly = true)
    override fun getStageBoard(stageId: Long, page: Int, size: Int, category: GameCategory?, sort: SortType): GetCommunityBoardResDto {
        communityValidator.validPageAndSize(page, size)
        return communityReader.readBoards(stageId, page, size, category, sort)
    }

    @Transactional(readOnly = true)
    override fun getStageBoardInfo(boardId: Long): GetCommunityBoardInfoResDto {
        val student = userUtil.getCurrentStudent()
        return communityReader.readBoardInfo(boardId, student)
    }

    @Transactional
    override fun likeStageBoard(boardId: Long): LikeResDto {
        val student = userUtil.getCurrentStudent()
        val board = communityReader.readBoardByBoardIdForWrite(boardId)
        return communityProcessor.likeBoard(student.studentId, board)
    }

    @Transactional
    override fun writeBoardComment(boardId: Long, writeBoardCommentDto: WriteBoardCommentReqDto): WriteBoardCommentResDto {
        val student = userUtil.getCurrentStudent()
        val board = communityReader.readBoardByBoardId(boardId)
        // 욕설 필터링 필요
        return communityProcessor.saveBoardComment(student, writeBoardCommentDto, board)
    }

    @Transactional
    override fun likeBoardComment(commentId: Long): LikeResDto {
        val student = userUtil.getCurrentStudent()
        val comment = communityReader.readCommentByCommentIdForWrite(commentId)
        return communityProcessor.likeBoardComment(student, comment)
    }

}