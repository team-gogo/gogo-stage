package gogo.gogostage.domain.community.board.persistence

import gogo.gogostage.domain.community.root.persistence.Community
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_board")
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    val community: Community,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "title", length = 30, nullable = false)
    val title: String,

    @Column(name = "content", length = 1000, nullable = false)
    val content: String,

    @Column(name = "comment_count", nullable = false)
    var commentCount: Int,

    @Column(name = "like_count", nullable = false)
    var likeCount: Int,

    @Column(name = "is_filtered", nullable = false)
    var isFiltered: Boolean,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "image_url", length = 800, nullable = true)
    val imageUrl: String?,
) {

    fun minusLikeCount() {
        likeCount -= 1
    }

    fun plusLikeCount() {
        likeCount += 1
    }

    fun plusCommentCount() {
        commentCount += 1
    }

    fun changeBoardFilter() {
        isFiltered = true
    }

}
