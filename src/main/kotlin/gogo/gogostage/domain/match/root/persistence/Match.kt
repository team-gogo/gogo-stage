package gogo.gogostage.domain.match.root.persistence

import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.team.root.persistence.Team
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_match")
class Match(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    val game: Game,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "a_team_id", nullable = false)
    val aTeam: Team,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_team_id", nullable = false)
    val bTeam: Team,

    @Enumerated(EnumType.STRING)
    @Column(name = "round", nullable = true)
    val round: Round?,

    @Column(name = "turn", nullable = true)
    val turn: Int?,

    @Column(name = "leagueTurn", nullable = true)
    val leagueTurn: Int?,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDateTime,

    @Column(name = "end_date", nullable = false)
    val endDate: LocalDateTime,

    @Column(name = "is_end", nullable = false)
    val isEnd: Boolean,

    @Column(name = "a_team_betting_point", nullable = false)
    var aTeamBettingPoint: Long,

    @Column(name = "b_team_betting_point", nullable = false)
    var bTeamBettingPoint: Long,
) {

    fun addATeamBettingPoint(bettingPoint: Long) {
        this.aTeamBettingPoint += bettingPoint
    }

    fun addBTeamBettingPoint(bettingPoint: Long) {
        this.bTeamBettingPoint += bettingPoint
    }

}

enum class Round {
    ROUND_OF_32, ROUND_OF_16, QUARTER_FINALS, SEMI_FINALS, FINALS
}
