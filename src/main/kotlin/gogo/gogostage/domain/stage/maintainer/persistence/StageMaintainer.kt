package gogo.gogostage.domain.stage.maintainer.persistence

import gogo.gogostage.domain.stage.root.persistence.Stage
import jakarta.persistence.*

@Entity
@Table(name = "tbl_stage_maintainer")
class StageMaintainer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false)
    val stage: Stage,

    @Column(name = "student_id", nullable = false)
    val studentId: Long
) {
    companion object {

        fun of(stage: Stage, studentId: Long) = StageMaintainer(
            stage = stage,
            studentId = studentId
        )

    }
}
