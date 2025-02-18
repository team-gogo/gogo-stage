package gogo.gogostage.domain.stage.participant.root.presentation

import gogo.gogostage.domain.stage.participant.root.application.ParticipantService
import gogo.gogostage.domain.stage.participant.root.application.dto.PointDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stage")
class StageParticipantController(
    private val participantService: ParticipantService
) {

    @GetMapping("/point/{stage_id}")
    fun point(
        @PathVariable("stage_id") stageId: Long,
        @RequestParam("studentId") studentId: Long
    ): ResponseEntity<PointDto> {
        val response = participantService.queryPoint(stageId, studentId)
        return ResponseEntity.ok(response)
    }

}
