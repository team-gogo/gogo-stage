package gogo.gogostage.domain.match.notification.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MatchNotificationRepository: JpaRepository<MatchNotification, Long> {
    @Query("SELECT mn FROM MatchNotification mn WHERE mn.match.id = :matchId AND mn.studentId = :studentId")
    fun findByMatchIdAndStudentIdForWrite(matchId: Long, studentId: Long): MatchNotification?
    fun existsByMatchIdAndStudentId(matchId: Long, studentId: Long): Boolean
}