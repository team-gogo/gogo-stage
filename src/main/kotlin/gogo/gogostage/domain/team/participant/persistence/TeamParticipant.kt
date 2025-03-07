package gogo.gogostage.domain.team.participant.persistence

import gogo.gogostage.domain.team.root.persistence.Team
import jakarta.persistence.*

@Entity
@Table(name = "tbl_team_participant")
class TeamParticipant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_participant_id")
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    val team: Team,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "position_x", nullable = true)
    val positionX: String?,

    @Column(name = "position_y", nullable = true)
    val positionY: String?,
)
