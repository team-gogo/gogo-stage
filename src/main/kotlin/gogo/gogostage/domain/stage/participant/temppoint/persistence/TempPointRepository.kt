package gogo.gogostage.domain.stage.participant.temppoint.persistence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface TempPointRepository: JpaRepository<TempPoint, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TempPoint t WHERE t.isApplied = false AND t.tempPointExpiredDate <= :now")
    fun findExpiredTempPoints(now: LocalDateTime): List<TempPoint>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TempPoint t WHERE t.isApplied = false AND t.tempPointExpiredDate > :now AND t.batchId = :batchId")
    fun findNotAppliedByBatchId(now: LocalDateTime, batchId: Long): List<TempPoint>
}
