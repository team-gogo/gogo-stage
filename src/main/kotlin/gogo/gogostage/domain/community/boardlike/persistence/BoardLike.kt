package gogo.gogostage.domain.community.boardlike.persistence

import gogo.gogostage.domain.community.board.persistence.Board
import jakarta.persistence.*

@Entity
@Table(name = "tbl_board_like")
class BoardLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    val board: Board,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,
)
