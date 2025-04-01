package gogo.gogostage.domain.community.comment.application

import gogo.gogostage.domain.community.comment.persistence.CommentRepository
import gogo.gogostage.global.error.StageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CommentReader(
    private val commentRepository: CommentRepository
) {

    fun read(commentId: Long) =
        commentRepository.findByIdOrNull(commentId)
            ?: throw StageException("Comment Not Found, commentId=$commentId", HttpStatus.NOT_FOUND.value())

    fun readForWrite(commentId: Long) =
        commentRepository.findByIdForWrite(commentId)
            ?: throw StageException("Comment Not Found, commentId=$commentId", HttpStatus.NOT_FOUND.value())

}