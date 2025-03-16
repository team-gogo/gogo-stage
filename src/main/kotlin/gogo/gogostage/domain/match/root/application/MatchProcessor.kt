package gogo.gogostage.domain.match.root.application

import gogo.gogostage.domain.match.notification.persistence.MatchNotification
import gogo.gogostage.domain.match.notification.persistence.MatchNotificationRepository
import gogo.gogostage.domain.match.root.application.dto.MatchToggleDto
import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class MatchProcessor(
    private val matchNotificationRepository: MatchNotificationRepository
) {

    fun toggleMatchNotification(match: Match, student: StudentByIdStub): MatchToggleDto {
        if (matchNotificationRepository.existsByMatchIdAndStudentId(match.id, student.studentId)) {
            val matchNotification = matchNotificationRepository.findByMatchIdAndStudentId(match.id, student.studentId)
                ?: throw StageException("MatchNotification Not Found, matchId=${match.id}, studentId=${student.studentId}", HttpStatus.NOT_FOUND.value())

            matchNotificationRepository.delete(matchNotification)

            return MatchToggleDto(
                isNotice = false
            )
        } else {
            val matchNotification = MatchNotification(
                match = match,
                studentId = student.studentId
            )

            matchNotificationRepository.save(matchNotification)

            return MatchToggleDto(
                isNotice = true
            )
        }
    }

}