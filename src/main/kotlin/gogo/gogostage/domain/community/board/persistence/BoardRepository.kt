package gogo.gogostage.domain.community.board.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long> {
}