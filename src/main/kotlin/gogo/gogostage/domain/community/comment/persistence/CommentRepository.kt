package gogo.gogostage.domain.community.comment.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {
    fun findByBoardIdAndStudentId(boardId: Long, studentId: Long): Comment?
}