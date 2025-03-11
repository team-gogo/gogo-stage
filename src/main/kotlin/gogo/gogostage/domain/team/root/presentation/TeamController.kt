import gogo.gogostage.domain.team.root.application.dto.TeamApplyDto

import gogo.gogostage.domain.team.root.application.TeamService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stage")
class TeamController(private val teamService: TeamService) {

    @PostMapping("/team/{game_id}")
    fun apply(
        @PathVariable("game_id") gameId: Long,
        @RequestBody @Valid dto: TeamApplyDto
    ): ResponseEntity<Unit> {
        teamService.apply(gameId, dto)
        return ResponseEntity(HttpStatus.CREATED)
    }

}
