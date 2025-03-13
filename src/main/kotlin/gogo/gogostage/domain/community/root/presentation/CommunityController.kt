package gogo.gogostage.domain.community.root.presentation

import gogo.gogostage.domain.community.root.application.CommunityService
import gogo.gogostage.domain.community.root.application.dto.WriteCommunityBoardDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stage/community")
class CommunityController(
    private val communityService: CommunityService
) {

    @PostMapping("/{stage_id}")
    fun writeStageBoard(
        @PathVariable("stage_id") stageId: Long,
        @RequestBody @Valid writeCommunityBoardDto: WriteCommunityBoardDto
    ): ResponseEntity<Void> {
        communityService.writeCommunityBoard(stageId, writeCommunityBoardDto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}