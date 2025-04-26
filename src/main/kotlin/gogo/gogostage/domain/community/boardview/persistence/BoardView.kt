package gogo.gogostage.domain.community.boardview.persistence

import gogo.gogostage.domain.community.board.persistence.Board
import jakarta.persistence.*

@Entity
@Table(name = "tbl_board_view")
class BoardView(

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