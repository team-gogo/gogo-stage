package gogo.gogostage.domain.community.root.event

data class CommentCreateEvent(
    val id: String,
    val commentId: Long,
    val content: String
)
