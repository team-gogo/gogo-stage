package gogo.gogostage.domain.coupon.result.persistence

import gogo.gogostage.domain.coupon.root.persistence.Coupon
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_coupon_result")
class CouponResult (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @JoinColumn(name = "coupon_id", nullable = false)
    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    val coupon: Coupon,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "is_gain", nullable = false)
    val isGain: Boolean,

    @Column(name = "before_point", nullable = false)
    val beforePoint: Long,

    @Column(name = "after_point", nullable = false)
    val afterPoint: Long,

    @Column(name = "create_at", nullable = false)
    val createAt: LocalDateTime = LocalDateTime.now(),

) {

    companion object {

        fun of(coupon: Coupon, studentId: Long, isGain: Boolean, beforePoint: Long, afterPoint: Long) = CouponResult(
            coupon = coupon,
            studentId = studentId,
            isGain = isGain,
            beforePoint = beforePoint,
            afterPoint = afterPoint
        )

    }

}
