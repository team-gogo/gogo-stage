package gogo.gogostage.domain.match.notification.application

import gogo.gogostage.domain.match.notification.persistence.MatchNotificationRepository
import org.springframework.stereotype.Component

@Component
class MatchNotificationReader(
    private val matchNotificationRepository: MatchNotificationRepository
) {

    fun readByMatchIdAndStudentIdForWrite(matchId: Long, studentId: Long) =
        matchNotificationRepository.findByMatchIdAndStudentIdForWrite(matchId, studentId)


}