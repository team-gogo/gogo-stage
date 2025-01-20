package gogo.gogostage.domain.community.like.persistence

import gogo.gogostage.domain.community.board.persistence.Board
import jakarta.persistence.*

@Entity
@Table(name = "tbl_like")
class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    val board: Board,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,
)
