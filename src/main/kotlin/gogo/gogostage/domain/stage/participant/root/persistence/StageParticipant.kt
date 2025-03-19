package gogo.gogostage.domain.stage.participant.root.persistence

import gogo.gogostage.domain.stage.root.persistence.Stage
import gogo.gogostage.global.error.StageException
import jakarta.persistence.*
import org.springframework.http.HttpStatus
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
        if (this.point - point < 0) {
            throw StageException("보유 포인트 보다 더 적게 배팅할 수 없습니다.", HttpStatus.BAD_REQUEST.value())
        }

        this.point -= point
    }

    fun plusPoint(point: Long) {
        this.point += point
    }

}
