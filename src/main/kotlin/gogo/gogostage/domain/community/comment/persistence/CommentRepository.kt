package gogo.gogostage.domain.community.comment.persistence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface CommentRepository: JpaRepository<Comment, Long> {
    fun findByBoardIdAndStudentId(boardId: Long, studentId: Long): Comment?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Comment c WHERE c.id = :commentId")
    fun findByIdForWrite(commentId: Long): Comment?

}