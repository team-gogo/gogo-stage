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
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_participant_id")
    val stageParticipant: StageParticipant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    val match: Match,

    @Column(name = "batch_id")
    val batchId: Long,

    @Column(name = "temp_point")
    val tempPoint: Long,

    @Column(name = "temp_point_expired_date")
    val tempPointExpiredDate: LocalDateTime,

    @Column(name = "is_applied")
    var isApplied: Boolean
) {

    fun applied() {
        isApplied = true
    }

    companion object {

        fun of(
            stageParticipant: StageParticipant,
            match: Match,
            batchId: Long,
            tempPoint: Long,
            tempPointExpiredDate: LocalDateTime
        ) = TempPoint(
            stageParticipant = stageParticipant,
            match = match,
            batchId = batchId,
            tempPoint = tempPoint,
            tempPointExpiredDate = tempPointExpiredDate,
            isApplied = false
        )

    }
}
