package gogo.gogostage.domain.coupon.root.persistence

import gogo.gogostage.domain.stage.root.persistence.Stage
import jakarta.persistence.*

@Entity
@Table(name = "tbl_coupon")
class Coupon (

    @Id
    @Column(name = "id", nullable = false)
    val id: String,

    @Column(name = "stage_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val stage: Stage,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: CouponType,

    @Column(name = "earn_point", nullable = false)
    val earnPoint: Long,

    @Column(name = "lost_point", nullable = true)
    val lostPoint: Long? = null,

    @Column(name = "is_used", nullable = false)
    var isUsed: Boolean = false,

) {

    companion object {

        fun ofNormal(id: String, stage: Stage, earnPoint: Long) = Coupon(
            id = id,
            type = CouponType.NORMAL,
            stage = stage,
            earnPoint = earnPoint,
        )


        fun ofRandom(id: String, stage: Stage, earnPoint: Long, lostPoint: Long) = Coupon(
            id = id,
            type = CouponType.RANDOM,
            stage = stage,
            earnPoint = earnPoint,
            lostPoint = lostPoint,
        )

    }

    fun used() {
        this.isUsed = true
    }

}

enum class CouponType {
    NORMAL, RANDOM
}
