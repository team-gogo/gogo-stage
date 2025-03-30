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
    val count: Int = 0,
    val team: List<GameTeamDto> = emptyList(),
)

data class GameTeamDto(
    val teamId: Long = 0L,
    val teamName: String = "",
    val participantCount: Int = 0,
    val winCount: Int = 0,
)

data class TeamInfoDto(
    val teamId: Long = 0L,
    val teamName: String = "",
    val participantCount: Int = 0,
    val participant: List<ParticipantDto> = emptyList(),
)

data class ParticipantDto(
    val studentId: Long = 0L,
    val name: String = "",
    val classNumber: Int = 0,
    val studentNumber: Int = 0,
    val positionX: String = "",
    val positionY: String = "",
)
