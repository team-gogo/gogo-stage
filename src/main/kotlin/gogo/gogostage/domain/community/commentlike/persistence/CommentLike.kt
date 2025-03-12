package gogo.gogostage.domain.community.commentlike.persistence

import gogo.gogostage.domain.community.comment.persistence.Comment
import jakarta.persistence.*

@Entity
@Table(name = "tbl_comment_like")
class CommentLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    val comment: Comment,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,
)
