package gogo.gogostage.domain.stage.rule.presentation

import gogo.gogostage.domain.stage.rule.application.StageRuleService
import gogo.gogostage.domain.stage.rule.application.dto.StageRuleDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stage")
class StageRuleController(
    private val stageRuleService: StageRuleService
) {

    @GetMapping("/rule/{stage_id}")
    fun query(
        @PathVariable("stage_id") stageId: String,
    ): ResponseEntity<StageRuleDto> {
        val response = stageRuleService.query(stageId)
        return ResponseEntity.ok(response)
    }

}
