package gogo.gogostage.domain.match.result.persistence

import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.team.root.persistence.Team
import jakarta.persistence.*

@Entity
@Table(name = "tbl_match_result")
class MatchResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    val match: Match,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "victory_team_id", nullable = false)
    val victoryTeam: Team,

    @Column(name = "a_team_score", nullable = false)
    val aTeamScore: Int,

    @Column(name = "b_team_score", nullable = false)
    val bTeamScore: Int
)
