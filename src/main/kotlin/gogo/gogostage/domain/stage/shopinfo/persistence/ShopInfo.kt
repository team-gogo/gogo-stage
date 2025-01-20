package gogo.gogostage.domain.stage.shopinfo.persistence

import gogo.gogostage.domain.stage.root.persistence.Stage
import jakarta.persistence.*

@Entity
@Table(name = "tbl_stage_shop_info")
class ShopInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false, unique = true)
    val stage: Stage,

    @Column(name = "is_sell_coin_toss_ticket", nullable = false)
    val isSellCoinTossTicket: Boolean,

    @Column(name = "is_sell_yavarwee_ticket", nullable = false)
    val isSellYavarweeTicket: Boolean,

    @Column(name = "is_sell_plinko_ticket", nullable = false)
    val isSellPlinkoTicket: Boolean,
)
