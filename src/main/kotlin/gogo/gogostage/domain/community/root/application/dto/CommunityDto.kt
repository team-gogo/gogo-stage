package gogo.gogostage.domain.community.root.application.dto

import gogo.gogostage.domain.community.root.persistence.SortType
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.domain.stage.root.persistence.StageType
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class WriteCommunityBoardDto(
    @NotNull
    val title: String,
    @NotNull
    val content: String,
    @NotNull
    val gameCategory: GameCategory
)

data class GetCommunityBoardReqDto(
    val page: Int,
    val size: Int,
    val gameCategory: GameCategory,
    val sort: SortType
)

data class GetCommunityBoardResDto(
    val info: InfoDto,
    val board: List<BoardDto>
)

data class InfoDto(
    val totalPage: Int,
    val totalElement: Int
)

data class BoardDto(
    val boardId: Long,
    val studentId: Long,
    val gameType: GameSystem,
    val title: String,
    val likeCount: Int,
    val createdAt: LocalDateTime,
    val isFiltered: Boolean,
    val stageType: StageType,
    val author: AuthorDto
)

data class AuthorDto(
    val studentId: Long,
    val name: String,
    val classNumber: Int,
    val studentNumber: Int
)
