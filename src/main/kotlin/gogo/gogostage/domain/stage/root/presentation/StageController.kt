package gogo.gogostage.domain.stage.root.presentation

import gogo.gogostage.domain.stage.root.application.StageService
import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

}
