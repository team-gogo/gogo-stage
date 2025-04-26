package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.board.application.BoardProcessor
import gogo.gogostage.domain.community.board.application.BoardReader
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.community.root.event.BoardCreateEvent
import gogo.gogostage.domain.community.root.event.CommentCreateEvent
import gogo.gogostage.domain.community.root.persistence.SortType
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CommunityServiceImpl(
    private val communityReader: CommunityReader,
    private val boardProcessor: BoardProcessor,
    private val userUtil: UserContextUtil,
    private val communityProcessor: CommunityProcessor,
    private val communityValidator: CommunityValidator,
    private val stageValidator: StageValidator,
    private val boardReader: BoardReader,
    private val applicationEventPublisher: ApplicationEventPublisher,
): CommunityService {

    @Transactional
    override fun writeCommunityBoard(stageId: Long, writeCommunityBoardDto: WriteCommunityBoardDto) {
        val student = userUtil.getCurrentStudent()
        stageValidator.validStage(student, stageId)
        val community = communityReader.readByStageIdAndGameCategory(stageId, writeCommunityBoardDto.gameCategory)
        val board = boardProcessor.saveCommunityBoard(community, student.studentId, writeCommunityBoardDto)

        applicationEventPublisher.publishEvent(
            BoardCreateEvent(
                id = UUID.randomUUID().toString(),
                boardId = board.id,
                content = board.title + " " + board.content
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getStageBoard(stageId: Long, page: Int, size: Int, category: GameCategory?, sort: SortType, isFiltered: Boolean, student: StudentByIdStub): GetCommunityBoardResDto {
        stageValidator.validStage(student, stageId)
        communityValidator.validPageAndSize(page, size)
        return communityReader.readBoards(isFiltered, stageId, page, size, category, sort)
    }

    @Transactional(readOnly = true)
    override fun getStageBoardInfo(boardId: Long, isFiltered: Boolean, student: StudentByIdStub): GetCommunityBoardInfoResDto {
        val board = boardReader.read(boardId)
        stageValidator.validStage(student, board.community.stage.id)
        stageValidator.validProfanityFilter(student, board)
        communityProcessor.saveBoardView(board, student.studentId)
        return communityReader.readBoardInfo(isFiltered, board, student)
    }

    @Transactional
    override fun likeStageBoard(boardId: Long): LikeResDto {
        val student = userUtil.getCurrentStudent()
        val board = communityReader.readBoardByBoardIdForWrite(boardId)
        stageValidator.validStage(student, board.community.stage.id)
        return communityProcessor.likeBoard(student.studentId, board)
    }

    @Transactional
    override fun writeBoardComment(boardId: Long, writeBoardCommentDto: WriteBoardCommentReqDto): WriteBoardCommentResDto {
        val student = userUtil.getCurrentStudent()
        val board = communityReader.readBoardByBoardId(boardId)
        stageValidator.validStage(student, board.community.stage.id)
        val writeBoardCommentResDto = communityProcessor.saveBoardComment(student, writeBoardCommentDto, board)

        applicationEventPublisher.publishEvent(
            CommentCreateEvent(
                id = UUID.randomUUID().toString(),
                commentId = writeBoardCommentResDto.commentId,
                content = writeBoardCommentResDto.content
            )
        )

        return writeBoardCommentResDto
    }

    @Transactional
    override fun likeBoardComment(commentId: Long): LikeResDto {
        val student = userUtil.getCurrentStudent()
        val comment = communityReader.readCommentByCommentIdForWrite(commentId)
        stageValidator.validStage(student, comment.board.community.stage.id)
        return communityProcessor.likeBoardComment(student, comment)
    }

}