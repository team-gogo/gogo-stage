package gogo.gogostage.domain.stage.root.persistence

import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_stage")
class Stage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Column(name = "school_id", nullable = false)
    val schoolId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: StageType,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "pass_code", nullable = true)
    val passCode: String?,

    @Column(name = "initial_point", nullable = false)
    val initialPoint: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: StageStatus,

    @Column(name = "participant_count", nullable = false)
    val participantCount: Int,

    @Column(name = "is_active_minigame", nullable = false)
    val isActiveMiniGame: Boolean,

    @Column(name = "is_active_shop", nullable = false)
    val isActiveShop: Boolean,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {

        fun fastOf(schoolId: Long, dto: CreateFastStageDto, isActiveMiniGame: Boolean) = Stage(
            schoolId = schoolId,
            type = StageType.FAST,
            name = dto.stageName,
            passCode = dto.passCode,
            initialPoint = dto.initialPoint,
            status = StageStatus.RECRUITING,
            participantCount = 0,
            isActiveMiniGame = isActiveMiniGame,
            isActiveShop = false,
        )

    }
}

enum class StageType {
    FAST, OFFICIAL
}

enum class StageStatus {
    RECRUITING, CONFIRMED, END
}
