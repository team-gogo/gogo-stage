package gogo.gogostage.domain.match.root.persistence

import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.team.root.persistence.Team
import gogo.gogostage.global.error.StageException
import jakarta.persistence.*
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_match")
class Match(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    val game: Game,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "a_team_id", nullable = true)
    var aTeam: Team? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_team_id", nullable = true)
    var bTeam: Team? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "round", nullable = true)
    val round: Round? = null,

    @Column(name = "turn", nullable = true)
    val turn: Int? = null,

    @Column(name = "leagueTurn", nullable = true)
    val leagueTurn: Int? = null,

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDateTime,

    @Column(name = "end_date", nullable = false)
    val endDate: LocalDateTime,

    @Column(name = "is_end", nullable = false)
    var isEnd: Boolean = false,

    @Column(name = "a_team_betting_point", nullable = false)
    var aTeamBettingPoint: Long = 0,

    @Column(name = "b_team_betting_point", nullable = false)
    var bTeamBettingPoint: Long = 0,
) {

    companion object {

        fun singleOf(
            game: Game,
            aTeam: Team,
            bTeam: Team,
            startDate: LocalDateTime,
            endDate: LocalDateTime
        ) = Match(
            game = game,
            aTeam = aTeam,
            bTeam = bTeam,
            startDate = startDate,
            endDate = endDate,
        )

        fun tournamentOf(
            game: Game,
            aTeam: Team?,
            bTeam: Team?,
            startDate: LocalDateTime,
            endDate: LocalDateTime,
            round: Round,
            turn: Int,
        ) = Match(
            game = game,
            aTeam = aTeam,
            bTeam = bTeam,
            startDate = startDate,
            endDate = endDate,
            round = round,
            turn = turn,
        )

        fun leagueOf(
            game: Game,
            aTeam: Team,
            bTeam: Team,
            startDate: LocalDateTime,
            endDate: LocalDateTime,
            leagueTurn: Int,
        ) = Match(
            game = game,
            aTeam = aTeam,
            bTeam = bTeam,
            startDate = startDate,
            endDate = endDate,
            leagueTurn = leagueTurn
        )

    }

    fun updateATeam(aTeam: Team?) {
        if (aTeam != null && this.aTeam != null) {
            throw StageException("A team already exists, Match Id = ${this.id}", HttpStatus.INTERNAL_SERVER_ERROR.value())
        }

        this.aTeam = aTeam
    }

    fun updateBTeam(bTeam: Team?) {
        if (bTeam != null && this.bTeam != null) {
            throw StageException("B team already exists, Match Id = ${this.id}", HttpStatus.INTERNAL_SERVER_ERROR.value())
        }

        this.bTeam = bTeam
    }

    fun end() {
        isEnd = true
    }

    fun batchRollback() {
        isEnd = false
    }

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
