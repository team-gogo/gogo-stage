package gogo.gogostage.domain.community.boardlike.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface BoardLikeRepository: JpaRepository<BoardLike, Long> {
    fun existsByStudentIdAndBoardId(studentId: Long, boardId: Long): Boolean
}