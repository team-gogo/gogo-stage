package gogo.gogostage.domain.stage.participant.root.presentation

import gogo.gogostage.domain.stage.participant.root.application.ParticipantService
import gogo.gogostage.domain.stage.participant.root.application.dto.MyTempPointDto
import gogo.gogostage.domain.stage.participant.root.application.dto.IsParticipantDto
import gogo.gogostage.domain.stage.participant.root.application.dto.PointDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stage")
class StageParticipantController(
    private val participantService: ParticipantService
) {

    @GetMapping("/api/point/{stage_id}")
    fun point(
        @PathVariable("stage_id") stageId: Long,
        @RequestParam("studentId") studentId: Long
    ): ResponseEntity<PointDto> {
        val response = participantService.queryPoint(stageId, studentId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/temp-point/me/{stage_id}")
    fun getMyTempPoint(
        @PathVariable("stage_id") stageId: Long,
    ): ResponseEntity<MyTempPointDto> {
        val response = participantService.getMyTempPoint(stageId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/participant/{stage_id}")
    fun isParticipant(
        @PathVariable("stage_id") stageId: Long,
        @RequestParam("studentId") studentId: Long
    ): ResponseEntity<IsParticipantDto> {
        val response = participantService.isParticipant(stageId, studentId)
        return ResponseEntity.ok(response)
    }

}
