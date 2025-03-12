package gogo.gogostage.domain.community.commentlike.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CommentLikeRepository: JpaRepository<CommentLike, Long> {
    fun existsByCommentIdAndStudentId(commentId: Long, studentId: Long): Boolean
    fun findByCommentIdAndStudentId(commentId: Long, studentId: Long): CommentLike?
}