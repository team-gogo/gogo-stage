package gogo.gogostage.domain.community.board.application

import gogo.gogostage.domain.community.board.persistence.BoardRepository
import gogo.gogostage.global.error.StageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class BoardReader(
    private val boardRepository: BoardRepository
) {

    fun read(boardId: Long) =
        boardRepository.findByIdOrNull(boardId)
            ?: throw StageException("Board Not Found, boardId=$boardId", HttpStatus.NOT_FOUND.value())

    fun readForWrite(boardId: Long) =
        boardRepository.findByIdForWrite(boardId)
            ?: throw StageException("Board Not Found, boardId=$boardId", HttpStatus.NOT_FOUND.value())

}