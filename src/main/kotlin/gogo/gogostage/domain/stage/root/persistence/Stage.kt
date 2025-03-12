package gogo.gogostage.domain.stage.root.persistence

import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.stage.maintainer.persistence.StageMaintainer
import gogo.gogostage.domain.stage.root.application.dto.CreateFastStageDto
import gogo.gogostage.domain.stage.root.application.dto.CreateOfficialStageDto
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
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

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

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

    @OneToMany(mappedBy = "stage", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    val maintainer: List<StageMaintainer> = listOf(),

    @OneToMany(mappedBy = "stage", cascade = [(CascadeType.ALL)], orphanRemoval = true)
    val game: List<Game> = listOf(),
) {
    companion object {

        fun fastOf(student: StudentByIdStub, dto: CreateFastStageDto, isActiveMiniGame: Boolean) = Stage(
            schoolId = student.schoolId,
            studentId = student.studentId,
            type = StageType.FAST,
            name = dto.stageName,
            passCode = dto.passCode,
            initialPoint = dto.initialPoint,
            status = StageStatus.RECRUITING,
            participantCount = 0,
            isActiveMiniGame = isActiveMiniGame,
            isActiveShop = false,
        )

        fun officialOf(student: StudentByIdStub, dto: CreateOfficialStageDto, isActiveMiniGame: Boolean, isActiveShop: Boolean) = Stage(
            schoolId = student.schoolId,
            studentId = student.studentId,
            type = StageType.OFFICIAL,
            name = dto.stageName,
            passCode = dto.passCode,
            initialPoint = dto.initialPoint,
            status = StageStatus.RECRUITING,
            participantCount = 0,
            isActiveMiniGame = isActiveMiniGame,
            isActiveShop = isActiveShop,
        )

    }
}

enum class StageType {
    FAST, OFFICIAL
}

enum class StageStatus {
    RECRUITING, CONFIRMED, END
}
