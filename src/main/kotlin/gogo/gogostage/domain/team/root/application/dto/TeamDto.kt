package gogo.gogostage.domain.team.root.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class TeamApplyDto(
    @NotBlank
    val teamName: String,
    @NotNull
    val participants: List<TeamParticipantDto>,
)

data class TeamParticipantDto(
    @NotNull
    val studentId: Long,
    @NotBlank
    val positionX: String,
    @NotBlank
    val positionY: String,
)

data class GameTeamResDto(
    val count: Int,
    val team: List<GameTeamDto>
)

data class GameTeamDto(
    val teamId: Long,
    val teamName: String,
    val participantCount: Int,
)

data class TeamInfoDto(
    val teamId: Long,
    val teamName: String,
    val participantCount: Int,
    val participant: List<ParticipantInfoDto>
)

data class ParticipantInfoDto(
    val studentId: Long,
    val name: String,
    val classNumber: Int,
    val studentNumber: Int,
    val positionX: String,
    val positionY: String,
)
