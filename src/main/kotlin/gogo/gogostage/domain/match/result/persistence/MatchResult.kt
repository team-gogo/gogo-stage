package gogo.gogostage.domain.match.result.persistence

import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.team.root.persistence.Team
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_match_result")
class MatchResult(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    val match: Match,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "victory_team_id", nullable = false)
    val victoryTeam: Team,

    @Column(name = "a_team_score", nullable = false)
    val aTeamScore: Int,

    @Column(name = "b_team_score", nullable = false)
    val bTeamScore: Int,

    @Column(name = "team_point_expired_date", nullable = false)
    val tempPointExpiredDate: LocalDateTime,

    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {

        fun of(match: Match, victoryTeam: Team, aTeamScore: Int, bTeamScore: Int, tempPointExpiredDate: LocalDateTime)= MatchResult(
            match = match,
            victoryTeam = victoryTeam,
            aTeamScore = aTeamScore,
            bTeamScore = bTeamScore,
            tempPointExpiredDate = tempPointExpiredDate,
        )

    }
}
