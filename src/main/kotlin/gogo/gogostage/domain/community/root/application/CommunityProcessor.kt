package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.board.persistence.Board
import gogo.gogostage.domain.community.boardlike.persistence.BoardLike
import gogo.gogostage.domain.community.boardlike.persistence.BoardLikeRepository
import gogo.gogostage.domain.community.root.application.dto.LikeBoardResDto
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CommunityProcessor(
    private val boardLikeRepository: BoardLikeRepository,
) {

    fun likeBoard(studentId: Long, board: Board): LikeBoardResDto {
        // 동시성 문제
        val isExistBoardLike = boardLikeRepository.existsByStudentIdAndBoardId(studentId, board.id)

        if (isExistBoardLike) {
            val boardLike = boardLikeRepository.findByBoardIdAndStudentId(board.id, studentId)
                ?: throw StageException("BoardLike Not Found, boardId=${board.id}, studentId=$studentId", HttpStatus.NOT_FOUND.value())

            boardLikeRepository.delete(boardLike)

            return LikeBoardResDto(
                isLiked = false
            )
        } else {
            val boardLike = BoardLike(
                board = board,
                studentId = studentId
            )

            boardLikeRepository.save(boardLike)

            return LikeBoardResDto(
                isLiked = true
            )
        }
    }
}