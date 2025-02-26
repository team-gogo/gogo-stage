package gogo.gogostage.domain.stage.participant.temppoint.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface TempPointRepository: JpaRepository<TempPoint, Long> {
    @Query("SELECT t FROM TempPoint t WHERE t.isApplied = false AND t.tempPointExpiredDate <= :now")
    fun findExpiredTempPoints(now: LocalDateTime): List<TempPoint>
}
