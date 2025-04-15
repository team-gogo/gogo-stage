package gogo.gogostage.domain.community.root.presentation

import gogo.gogostage.domain.community.root.application.CommunityService
import gogo.gogostage.domain.community.root.application.dto.*
import gogo.gogostage.domain.community.root.persistence.SortType
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.global.util.UserContextUtil
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stage/community")
class CommunityController(
    private val communityService: CommunityService,
    private val userUtil: UserContextUtil
) {

    @PostMapping("/{stage_id}")
    fun writeStageBoard(
        @PathVariable("stage_id") stageId: Long,
        @RequestBody @Valid writeCommunityBoardDto: WriteCommunityBoardDto
    ): ResponseEntity<Void> {
        communityService.writeCommunityBoard(stageId, writeCommunityBoardDto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/{stage_id}")
    fun getStageBoard(
        @PathVariable("stage_id") stageId: Long,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "20") size: Int,
        @RequestParam(required = false) category: GameCategory? = null,
        @RequestParam(required = false) sort: SortType = SortType.LATEST
    ): ResponseEntity<GetCommunityBoardResDto> {
        val student = userUtil.getCurrentStudent()
        val isFiltered = student.isActiveProfanityFilter
        val response = communityService.getStageBoard(
            stageId, page, size, category, sort, isFiltered, student)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/board/{board_id}")
    fun getStageBoardInfo(
        @PathVariable("board_id") boardId: Long
    ): ResponseEntity<GetCommunityBoardInfoResDto> {
        val student = userUtil.getCurrentStudent()
        val isFiltered = student.isActiveProfanityFilter
        val response = communityService.getStageBoardInfo(boardId, isFiltered, student)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/board/like/{board_id}")
    fun likeStageBoard(
        @PathVariable("board_id") boardId: Long
    ): ResponseEntity<LikeResDto> {
        val response = communityService.likeStageBoard(boardId)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/comment/{board_id}")
    fun writeBoardComment(
        @PathVariable("board_id") boardId: Long,
        @RequestBody @Valid writeBoardCommentDto: WriteBoardCommentReqDto
    ): ResponseEntity<WriteBoardCommentResDto> {
        val response = communityService.writeBoardComment(boardId, writeBoardCommentDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/comment/like/{comment_id}")
    fun likeBoardComment(
        @PathVariable("comment_id") commentId: Long
    ): ResponseEntity<LikeResDto> {
        val response = communityService.likeBoardComment(commentId)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}