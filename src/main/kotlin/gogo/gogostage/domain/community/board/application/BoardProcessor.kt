package gogo.gogostage.domain.community.board.application

import gogo.gogostage.domain.community.board.persistence.Board
import gogo.gogostage.domain.community.board.persistence.BoardRepository
import gogo.gogostage.domain.community.root.application.dto.WriteCommunityBoardDto
import gogo.gogostage.domain.community.root.persistence.Community
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BoardProcessor(
    private val boardRepository: BoardRepository
) {

    fun saveCommunityBoard(
        community: Community,
        studentId: Long,
        writeCommunityBoardDto: WriteCommunityBoardDto,

    ): Board {
        val board = Board(
            community = community,
            studentId = studentId,
            title = writeCommunityBoardDto.title,
            content = writeCommunityBoardDto.content,
            commentCount = 0,
            likeCount = 0,
            isFiltered = false,
            createdAt = LocalDateTime.now(),
            imageUrl = writeCommunityBoardDto.imageUrl,
            viewCount = 0
        )

        return boardRepository.save(board)
    }

}