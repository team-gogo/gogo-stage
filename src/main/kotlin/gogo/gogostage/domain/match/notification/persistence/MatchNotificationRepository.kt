package gogo.gogostage.domain.match.notification.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface MatchNotificationRepository: JpaRepository<MatchNotification, Long> {
    fun findByMatchIdAndStudentId(matchId: Long, studentId: Long): MatchNotification?
    fun existsByMatchIdAndStudentId(matchId: Long, studentId: Long): Boolean
}