package gogo.gogostage.domain.community.root.event

data class BoardCreateEvent(
    val id: String,
    val boardId: Long,
    val content: String
)
