package gogo.gogostage.domain.match.notification.application

import gogo.gogostage.domain.match.notification.persistence.MatchNotificationRepository
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class MatchNotificationReader(
    private val matchNotificationRepository: MatchNotificationRepository
) {

    fun readByMatchIdAndStudentIdForWrite(matchId: Long, studentId: Long) =
        matchNotificationRepository.findByMatchIdAndStudentIdForWrite(matchId, studentId)
            ?: throw StageException("MatchNotification Not Found, matchId=$matchId, studentId=$studentId", HttpStatus.NOT_FOUND.value())

}