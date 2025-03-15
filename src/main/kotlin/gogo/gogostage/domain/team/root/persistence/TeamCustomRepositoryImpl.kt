package gogo.gogostage.domain.team.root.persistence

import gogo.gogostage.domain.team.root.application.dto.ParticipantInfoDto
import gogo.gogostage.domain.team.root.application.dto.TeamInfoDto
import gogo.gogostage.global.internal.student.api.StudentApi
import org.springframework.stereotype.Repository

@Repository
class TeamCustomRepositoryImpl(
    private val studentApi: StudentApi
): TeamCustomRepository {

    override fun queryTeamInfo(team: Team): TeamInfoDto {
        val studentIds = team.participants.map { it.studentId }

        val students = studentApi.queryByStudentsIds(studentIds).students

        val participantInfoDto = students.map { student ->
            val participant = team.participants.find { it.studentId == student.studentId }!!

            ParticipantInfoDto(
                studentId = student.studentId,
                name = student.name,
                classNumber = student.classNumber,
                studentNumber = student.studentNumber,
                positionX = participant.positionX!!,
                positionY = participant.positionY!!
            )
        }

        return TeamInfoDto(
            teamId = team.id,
            teamName = team.name,
            participantCount = team.participantCount,
            participant = participantInfoDto,
        )
    }

}