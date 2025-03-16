package gogo.gogostage.domain.match.notification.persistence

import gogo.gogostage.domain.match.root.persistence.Match
import jakarta.persistence.*

@Entity
@Table(name = "tbl_match_notification")
class MatchNotification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    val match: Match,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,
) {
}




