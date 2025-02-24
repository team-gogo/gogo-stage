package gogo.gogostage.domain.match.root.presentation

import gogo.gogostage.domain.match.root.application.MatchService
import gogo.gogostage.domain.match.root.application.dto.MatchApiInfoDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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

}
