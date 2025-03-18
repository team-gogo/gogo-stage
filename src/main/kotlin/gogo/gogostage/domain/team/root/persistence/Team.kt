package gogo.gogostage.domain.team.root.persistence

import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.team.participant.persistence.TeamParticipant
import jakarta.persistence.*

@Entity
@Table(name = "tbl_team")
class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    val game: Game,

    @Column(name = "win_count", nullable = false)
    var winCount: Int = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "participant_count", nullable = false)
    val participantCount: Int,

    @Column(name = "is_participating", nullable = false)
    var isParticipating: Boolean = false,

    @OneToMany(mappedBy = "team")
    val participants: MutableList<TeamParticipant> = mutableListOf(),
) {
    companion object {

        fun of (game: Game, name: String, participantCount: Int) = Team(
            game = game,
            name = name,
            participantCount = participantCount
        )

    }

    fun participation(): Int {
        if (isParticipating) {
            return 0
        } else {
            this.isParticipating = true
            return 1
        }
    }

    fun addWinCount() {
        this.winCount++
    }

}
