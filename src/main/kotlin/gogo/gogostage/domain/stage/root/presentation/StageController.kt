package gogo.gogostage.domain.stage.root.presentation

import gogo.gogostage.domain.stage.root.application.StageService
import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import gogo.gogostage.domain.stage.root.application.dto.StageConfirmDto
import gogo.gogostage.domain.stage.root.application.dto.StageJoinDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stage")
class StageController(
    private val stageService: StageService,
) {

    @PostMapping("/fast")
    fun createFast(
        @RequestBody @Valid dto: CreateFastStageDto,
    ): ResponseEntity<Unit> {
        stageService.createFast(dto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/official")
    fun createFast(
        @RequestBody @Valid dto: CreateOfficialStageDto,
    ): ResponseEntity<Unit> {
        stageService.createOfficial(dto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/join/{stage_id}")
    fun createFast(
        @PathVariable("stage_id") stageId: Long,
        @RequestBody dto: StageJoinDto,
    ): ResponseEntity<Unit> {
        stageService.join(stageId, dto)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/confirm/{stage_id}")
    fun confirm(
        @PathVariable("stage_id") stageId: Long,
        @RequestBody dto: StageConfirmDto,
    ): ResponseEntity<Unit> {
        stageService.confirm(stageId, dto)
        return ResponseEntity.ok().build()
    }

}
