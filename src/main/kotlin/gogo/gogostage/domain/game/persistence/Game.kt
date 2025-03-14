package gogo.gogostage.domain.game.persistence

import gogo.gogostage.domain.stage.root.persistence.Stage
import gogo.gogostage.domain.team.root.persistence.Team
import jakarta.persistence.*

@Entity
@Table(name = "tbl_game")
class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false)
    val stage: Stage,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    val category: GameCategory,

    @Column(name = "name", nullable = true)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "system", nullable = false)
    val system: GameSystem,

    @Column(name = "team_min_capacity", nullable = false)
    val teamMinCapacity: Int,

    @Column(name = "team_max_capacity", nullable = false)
    val teamMaxCapacity: Int,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_place_team_id", nullable = true)
    var firstPlaceTeam: Team? = null,

    @Column(name = "team_count", nullable = false)
    val teamCount: Int = 0, // 팀 신청시 ++

    @Column(name = "league_count", nullable = true)
    var leagueCount: Int? = null,

    @Column(name = "is_end", nullable = false)
    var isEnd: Boolean = false
){
    companion object {

        fun of(
            stage: Stage,
            category: GameCategory,
            name: String,
            system: GameSystem,
            teamMinCapacity: Int,
            teamMaxCapacity: Int,
        ) = Game(
            stage = stage,
            category = category,
            name = name,
            system = system,
            teamMinCapacity = teamMinCapacity,
            teamMaxCapacity = teamMaxCapacity,
        )

    }

    fun updateLeagueCount(leagueCount: Int) {
        this.leagueCount = leagueCount
    }

    fun end(firstPlaceTeam: Team?) {
        this.isEnd = true
        this.firstPlaceTeam = firstPlaceTeam
    }

    fun gameEndRollBack() {
        this.isEnd = false
        this.firstPlaceTeam = null
    }

}

enum class GameCategory {
    SOCCER, BASKET_BALL, BASE_BALL, VOLLEY_BALL, BADMINTON, LOL, ETC
}

enum class GameSystem {
    TOURNAMENT, FULL_LEAGUE, SINGLE
}
