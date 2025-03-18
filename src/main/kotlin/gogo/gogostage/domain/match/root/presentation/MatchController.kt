package gogo.gogostage.domain.match.root.presentation

import gogo.gogostage.domain.match.root.application.MatchService
import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchInfoDto
import gogo.gogostage.domain.match.root.application.dto.MatchSearchDto
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stage")
class MatchController(
    private val matchService: MatchService,
) {

    @GetMapping("/api/match/info")
    fun matchApiInfo(
        @RequestParam matchId: Long
    ): ResponseEntity<MatchApiInfoDto> {
        val response = matchService.matchApiInfo(matchId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/match/search/{stage_id}")
    fun search(
        @PathVariable("stage_id") stageId: Long,
        @RequestParam @Valid @NotNull y: Int,
        @RequestParam @Valid @NotNull m: Int,
        @RequestParam @Valid @NotNull d: Int,
    ): ResponseEntity<MatchSearchDto> {
        val response = matchService.search(stageId, y, m, d)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/match/me/{stage_id}")
    fun me(
        @PathVariable("stage_id") stageId: Long,
    ): ResponseEntity<MatchSearchDto> {
        val response = matchService.me(stageId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/match/info/{match_id}")
    fun info(
        @PathVariable("match_id") matchId: Long,
    ): ResponseEntity<MatchInfoDto> {
        val response = matchService.info(matchId)
        return ResponseEntity.ok(response)
    }

}
