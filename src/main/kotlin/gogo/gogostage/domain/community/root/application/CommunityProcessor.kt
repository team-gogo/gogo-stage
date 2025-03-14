package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.board.persistence.Board
import gogo.gogostage.domain.community.board.persistence.BoardRepository
import gogo.gogostage.domain.community.boardlike.persistence.BoardLike
import gogo.gogostage.domain.community.boardlike.persistence.BoardLikeRepository
import gogo.gogostage.domain.community.comment.persistence.Comment
import gogo.gogostage.domain.community.comment.persistence.CommentRepository
import gogo.gogostage.domain.community.commentlike.persistence.CommentLike
import gogo.gogostage.domain.community.commentlike.persistence.CommentLikeRepository
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CommunityProcessor(
    private val boardLikeRepository: BoardLikeRepository,
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val commentMapper: CommunityMapper,
    private val boardRepository: BoardRepository
) {

    fun likeBoard(studentId: Long, board: Board): LikeResDto {
        // 동시성 문제
        val isExistBoardLike = boardLikeRepository.existsByStudentIdAndBoardId(studentId, board.id)
        val comment = commentRepository.findByBoardIdAndStudentId(board.id, studentId)
            ?: throw StageException("Comment Not Found, boardId=${board.id}, studentId=${studentId}", HttpStatus.NOT_FOUND.value())

        if (isExistBoardLike) {
            val boardLike = boardLikeRepository.findByBoardIdAndStudentId(board.id, studentId)
                ?: throw StageException("BoardLike Not Found", HttpStatus.NOT_FOUND.value())

            boardLikeRepository.delete(boardLike)

            board.minusLikeCount()
            boardRepository.save(board)

            return LikeResDto(
                isLiked = false
            )
        } else {
            val boardLike = BoardLike(
                board = board,
                studentId = studentId
            )

            boardLikeRepository.save(boardLike)

            board.plusLikeCount()
            boardRepository.save(board)

            return LikeResDto(
                isLiked = true
            )
        }
    }

    fun saveBoardComment(
        student: StudentByIdStub,
        writeBoardCommentDto: WriteBoardCommentReqDto,
        board: Board
    ): WriteBoardCommentResDto {
        val comment = Comment(
            board = board,
            studentId = student.studentId,
            content = writeBoardCommentDto.content,
            isFiltered = false,
            createdAt = LocalDateTime.now(),
            likeCount = 0
        )

        commentRepository.save(comment)

        return commentMapper.mapWriteBoardCommentResDto(comment, writeBoardCommentDto, student)
    }

    fun likeBoardComment(student: StudentByIdStub, comment: Comment): LikeResDto {
        val isExistCommentLike = commentLikeRepository.existsByCommentIdAndStudentId(comment.id, student.studentId)

        if (isExistCommentLike) {
            val commentLike = commentLikeRepository.findByCommentIdAndStudentId(comment.id, student.studentId)
                ?: throw StageException("CommentLike Not Found, commentId=${comment.id}, studentId=${student.studentId}", HttpStatus.NOT_FOUND.value())

            commentLikeRepository.delete(commentLike)

            comment.minusLikeCount()
            commentRepository.save(comment)

            return LikeResDto(
                isLiked = false
            )
        } else {
            val boardLike = CommentLike(
                comment = comment,
                studentId = student.studentId,
            )

            commentLikeRepository.save(boardLike)

            comment.plusLikeCount()
            commentRepository.save(comment)

            return LikeResDto(
                isLiked = true
            )
        }
    }
}