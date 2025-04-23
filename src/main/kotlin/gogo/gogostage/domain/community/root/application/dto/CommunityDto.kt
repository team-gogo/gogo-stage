package gogo.gogostage.domain.community.root.application.dto

import com.fasterxml.jackson.annotation.JsonFormat
import gogo.gogostage.domain.game.persistence.GameCategory
import gogo.gogostage.domain.stage.root.persistence.StageType
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

data class WriteCommunityBoardDto(
    @NotNull
    @Size(max = 30)
    val title: String,
    @NotNull
    @Size(max = 1000)
    val content: String,
    @NotNull
    val gameCategory: GameCategory,
    val imageUrl: String?,
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
    val gameCategory: GameCategory,
    val title: String,
    val likeCount: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    val stageType: StageType,
    val commentCount: Int,
)

data class GetCommunityBoardInfoResDto(
    val boardId: Long,
    val title: String,
    val content: String,
    val likeCount: Int,
    val isLiked: Boolean,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    val imageUrl: String?,
    val stage: StageDto,
    val commentCount: Int,
    val comment: List<CommentDto>
)

data class StageDto(
    val name: String,
    val category: GameCategory
)

data class CommentDto(
    val commentId: Long,
    val content: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    val likeCount: Int,
    val isLiked: Boolean,
)

data class LikeResDto(
    val isLiked: Boolean,
)

data class WriteBoardCommentReqDto(
    @NotNull
    @Size(max = 300)
    val content: String,
)

data class WriteBoardCommentResDto(
    val commentId: Long,
    val content: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    val likeCount: Int,
)
