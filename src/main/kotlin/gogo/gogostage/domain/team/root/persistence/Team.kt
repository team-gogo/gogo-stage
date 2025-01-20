package gogo.gogostage.domain.team.root.persistence

import gogo.gogostage.domain.game.persistence.Game
import jakarta.persistence.*

@Entity
@Table(name = "tbl_team")
class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    val game: Game,

    @Column(name = "captain_student_id", nullable = false)
    val captainStudentId: Long,

    @Column(name = "win_count", nullable = false)
    val winCount: Int,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "participant_count", nullable = false)
    val participantCount: Int,

    @Column(name = "is_participating", nullable = false)
    val isParticipating: Boolean,
)
