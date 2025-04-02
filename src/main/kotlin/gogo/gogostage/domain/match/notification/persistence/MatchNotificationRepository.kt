package gogo.gogostage.domain.match.notification.persistence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface MatchNotificationRepository: JpaRepository<MatchNotification, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT mn FROM MatchNotification mn WHERE mn.match.id = :matchId AND mn.studentId = :studentId")
    fun findByMatchIdAndStudentIdForWrite(matchId: Long, studentId: Long): MatchNotification?
    fun existsByMatchIdAndStudentId(matchId: Long, studentId: Long): Boolean
}