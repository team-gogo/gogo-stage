package gogo.gogostage.domain.stage.participant.temppoint.persistence

import gogo.gogostage.domain.match.root.persistence.Match
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipant
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_temp_point")
class TempPoint(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_participant_id")
    val stageParticipant: StageParticipant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    val match: Match,

    @Column(name = "batch_id")
    val batchId: Long,

    @Column(name = "temp_point")
    val tempPoint: Int,

    @Column(name = "temp_point_expired_date")
    val tempPointExpiredDate: LocalDateTime,

    @Column(name = "is_applied")
    val isApplied: Boolean
)
