package gogo.gogostage.domain.game.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<Game, Long> {
    fun countByStageId(stageId: Long): Int
}
