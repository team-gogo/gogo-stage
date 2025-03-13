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
    @Column(name = "type", nullable = false)
    val system: GameSystem,

    @Column(name = "team_min_capacity", nullable = false)
    val teamMinCapacity: Int,

    @Column(name = "team_max_capacity", nullable = false)
    val teamMaxCapacity: Int,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_place_team_id", nullable = true)
    val firstPlaceTeam: Team? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_place_team_id", nullable = true)
    val secondPlaceTeam: Team? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "third_place_team_id", nullable = true)
    val thirdPlaceTeam: Team? = null,

    @Column(name = "team_count", nullable = false)
    val teamCount: Int = 0,

    @Column(name = "is_end", nullable = false)
    val isEnd: Boolean = false
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
}

enum class GameCategory {
    SOCCER, BASKET_BALL, BASE_BALL, VOLLEY_BALL, BADMINTON, LOL, ETC
}

enum class GameSystem {
    TOURNAMENT, FULL_LEAGUE, SINGLE
}
