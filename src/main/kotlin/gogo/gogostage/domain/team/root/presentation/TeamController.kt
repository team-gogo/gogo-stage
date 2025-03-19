package gogo.gogostage.domain.team.root.presentation

import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto

import gogo.gogostage.domain.team.root.application.TeamService
import gogo.gogostage.domain.team.root.application.dto.GameTeamResDto
import gogo.gogostage.domain.team.root.persistence.Team
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stage")
class TeamController(
    private val teamService: TeamService
) {

    @PostMapping("/team/{game_id}")
    fun apply(
        @PathVariable("game_id") gameId: Long,
        @RequestBody @Valid dto: TeamApplyDto
    ): ResponseEntity<Unit> {
        teamService.apply(gameId, dto)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping("/team/{game_id}")
    fun getGameTeam(
        @PathVariable("game_id") gameId: Long
    ): ResponseEntity<GameTeamResDto> {
        val response = teamService.getGameTeam(gameId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/team/temp/{game_id}")
    fun getGameTempTeam(
        @PathVariable("game_id") gameId: Long
    ): ResponseEntity<GameTeamResDto> {
        val response = teamService.getGameTempTeam(gameId)
        return ResponseEntity.ok(response)
    }

}
