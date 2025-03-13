package gogo.gogostage.domain.stage.participant.root.persistence

import gogo.gogostage.domain.stage.root.persistence.Stage
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_stage_participant")
class StageParticipant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false)
    val stage: Stage,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "point", nullable = false)
    var point: Long,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {

    companion object {

        fun of (stage: Stage, studentId: Long, point: Long) = StageParticipant(
            stage = stage,
            studentId = studentId,
            point = point
        )

    }

    fun minusPoint(point: Long) {
        this.point -= point
    }

    fun plusPoint(point: Long) {
        this.point += point
    }

}
