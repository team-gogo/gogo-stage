package gogo.gogostage.domain.game.presentation

import gogo.gogostage.domain.game.application.GameService
import gogo.gogostage.domain.game.application.dto.QueryGameDto
import gogo.gogostage.domain.game.application.dto.QueryGameFormatDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stage")
class GameController(
    private val gameService: GameService
) {

    @GetMapping("/game/{stage_id}")
    fun all(
        @PathVariable("stage_id") stageId: Long
    ): ResponseEntity<QueryGameDto> {
        val response = gameService.queryAll(stageId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/game/format/{game_id}")
    fun format(
        @PathVariable("game_id") gameId: Long
    ): ResponseEntity<QueryGameFormatDto> {
        val response = gameService.queryFormat(gameId)
        return ResponseEntity.ok(response)
    }

}
