package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.board.application.BoardProcessor
import gogo.gogostage.domain.community.board.application.BoardReader
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.community.root.persistence.SortType
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommunityServiceImpl(
    private val communityReader: CommunityReader,
    private val boardProcessor: BoardProcessor,
    private val userUtil: UserContextUtil,
    private val communityProcessor: CommunityProcessor,
    private val communityValidator: CommunityValidator,
    private val stageValidator: StageValidator,
    private val boardReader: BoardReader
): CommunityService {

    @Transactional
    override fun writeCommunityBoard(stageId: Long, writeCommunityBoardDto: WriteCommunityBoardDto) {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        val community = communityReader.readByStageIdAndGameCategory(stageId, writeCommunityBoardDto.gameCategory)
        boardProcessor.saveCommunityBoard(community, student.studentId, writeCommunityBoardDto)

        // 추후 욕설 필터링 요청 처리 필요
    }

    @Transactional(readOnly = true)
    override fun getStageBoard(stageId: Long, page: Int, size: Int, category: GameCategory?, sort: SortType): GetCommunityBoardResDto {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        communityValidator.validPageAndSize(page, size)
        return communityReader.readBoards(stageId, page, size, category, sort)
    }

    @Transactional(readOnly = true)
    override fun getStageBoardInfo(boardId: Long): GetCommunityBoardInfoResDto {
        val student = userUtil.getCurrentStudent()
        val board = boardReader.read(boardId)
        stageValidator.validStage(student, board.community.stage.id)
        return communityReader.readBoardInfo(board, student)
    }

    @Transactional
    override fun likeStageBoard(boardId: Long): LikeResDto {
        val student = userUtil.getCurrentStudent()
        val board = communityReader.readBoardByBoardId(boardId)
        stageValidator.validStage(student, board.community.stage.id)
        return communityProcessor.likeBoard(student.studentId, board)
    }

    @Transactional
    override fun writeBoardComment(boardId: Long, writeBoardCommentDto: WriteBoardCommentReqDto): WriteBoardCommentResDto {
        val student = userUtil.getCurrentStudent()
        val board = communityReader.readBoardByBoardId(boardId)
        stageValidator.validStage(student, board.community.stage.id)
        // 욕설 필터링 필요
        return communityProcessor.saveBoardComment(student, writeBoardCommentDto, board)
    }

    @Transactional
    override fun likeBoardComment(commentId: Long): LikeResDto {
        val student = userUtil.getCurrentStudent()
        val comment = communityReader.readCommentByCommentId(commentId)
        stageValidator.validStage(student, comment.board.community.stage.id)
        return communityProcessor.likeBoardComment(student, comment)
    }

}