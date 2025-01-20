package gogo.gogostage.domain.stage.minigameinfo.persistence

import gogo.gogostage.domain.stage.root.persistence.Stage
import jakarta.persistence.*

@Entity
@Table(name = "tbl_stage_minigame_info")
class MiniGameInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false, unique = true)
    val stage: Stage,

    @Column(name = "is_active_coin_toss", nullable = false)
    val isActiveCoinToss: Boolean,

    @Column(name = "is_active_yavarwee", nullable = false)
    val isActiveYavarwee: Boolean,

    @Column(name = "is_active_plinko", nullable = false)
    val isActivePlinko: Boolean,
)
