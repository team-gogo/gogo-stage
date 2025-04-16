package gogo.gogostage.domain.community.comment.persistence

import gogo.gogostage.domain.community.board.persistence.Board
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    val board: Board,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "content", length = 300, nullable = false)
    val content: String,

    @Column(name = "is_filtered", nullable = false)
    var isFiltered: Boolean,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "like_count", nullable = false)
    var likeCount: Int,
) {

    fun plusLikeCount() {
        likeCount += 1
    }

    fun minusLikeCount() {
        likeCount -= 1
    }

    fun changeIsFiltered() {
        isFiltered = true
    }
}
