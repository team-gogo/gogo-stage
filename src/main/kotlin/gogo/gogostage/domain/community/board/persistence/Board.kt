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

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "content", nullable = false)
    val content: String,

    @Column(name = "comment_count", nullable = false)
    val commentCount: Int,

    @Column(name = "like_count", nullable = false)
    val likeCount: Int,

    @Column(name = "is_filtered", nullable = false)
    val isFiltered: Boolean,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime
)
