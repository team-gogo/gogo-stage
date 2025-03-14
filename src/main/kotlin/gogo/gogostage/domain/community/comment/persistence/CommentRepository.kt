package gogo.gogostage.domain.community.comment.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {
}