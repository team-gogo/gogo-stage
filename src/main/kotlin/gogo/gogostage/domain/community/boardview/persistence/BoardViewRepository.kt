package gogo.gogostage.domain.community.boardview.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface BoardViewRepository: JpaRepository<BoardView, Long> {

    fun existsByBoardIdAndStudentId(boardId: Long, studentId: Long): Boolean
}