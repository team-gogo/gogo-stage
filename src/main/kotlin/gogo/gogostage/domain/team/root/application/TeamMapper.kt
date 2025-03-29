package gogo.gogostage.domain.team.root.application

import gogo.gogostage.domain.team.root.application.dto.GameTeamDto
import gogo.gogostage.domain.team.root.application.dto.GameTeamResDto
import gogo.gogostage.domain.team.root.application.dto.ParticipantDto
import gogo.gogostage.domain.team.root.application.dto.TeamInfoDto
import gogo.gogostage.domain.team.root.persistence.Team
import gogo.gogostage.global.internal.student.stub.StudentByIdsStubDto
import org.springframework.stereotype.Component

@Component
class TeamMapper {

    fun mapGameTeam(teams: List<Team>): GameTeamResDto {
        val gameTeamListDto = teams.map {GameTeamDto(
                teamId = it.id,
                teamName = it.name,
                participantCount = it.participantCount,
                winCount = it.winCount,
            )}

        return GameTeamResDto(
            count = gameTeamListDto.count(),
            team = gameTeamListDto
        )
    }

    fun mapStudentIds(team: Team): List<Long> =
        team.participants.map { it.studentId }

    fun mapParticipantInfoDto(team: Team, students: List<StudentByIdsStubDto>): List<ParticipantDto> =
        students.map { student ->
            val participant = team.participants.find { it.studentId == student.studentId}!!

            ParticipantDto(
                studentId = student.studentId,
                name = student.name,
                classNumber = student.classNumber,
                studentNumber = student.studentNumber,
                positionX = participant.positionX!!,
                positionY = participant.positionY!!
            )
        }

    fun mapTeamInfoDto(team: Team, participantDto: List<ParticipantDto>): TeamInfoDto =
        TeamInfoDto(
            teamId = team.id,
            teamName = team.name,
            participantCount = team.participantCount,
            participant = participantDto
        )

}