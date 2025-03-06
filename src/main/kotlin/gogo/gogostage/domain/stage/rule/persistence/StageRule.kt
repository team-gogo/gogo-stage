package gogo.gogostage.domain.stage.rule.persistence

import gogo.gogostage.domain.stage.root.application.dto.CreateStageRuleDto
import gogo.gogostage.domain.stage.root.persistence.Stage
import jakarta.persistence.*

@Entity
@Table(name = "tbl_stage_rule")
class StageRule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false, unique = true)
    val stage: Stage,

    @Column(name = "max_betting_point", nullable = false)
    val maxBettingPoint: Long,

    @Column(name = "min_betting_point", nullable = false)
    val minBettingPoint: Long
) {
    companion object {

        fun of(stage: Stage, dto: CreateStageRuleDto) = StageRule(
            stage = stage,
            maxBettingPoint = dto.maxBettingPoint,
            minBettingPoint = dto.minBettingPoint
        )

    }
}
