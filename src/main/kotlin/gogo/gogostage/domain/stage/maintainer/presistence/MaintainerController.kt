package gogo.gogostage.domain.stage.maintainer.presistence

import gogo.gogostage.domain.stage.maintainer.application.MaintainerService
import gogo.gogostage.domain.stage.maintainer.application.dto.IsMaintainerDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stage/maintainer")
class MaintainerController(
    private val maintainerService: MaintainerService,
) {

    @GetMapping
    fun isMaintainer(
        @RequestParam matchId: Long,
        @RequestParam studentId: Long,
    ): ResponseEntity<IsMaintainerDto> {
        val response = maintainerService.isMaintainer(matchId, studentId)
        return ResponseEntity.ok(response)
    }

}
