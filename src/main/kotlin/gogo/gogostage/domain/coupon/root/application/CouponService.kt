package gogo.gogostage.domain.coupon.root.application

import gogo.gogostage.domain.coupon.root.application.dto.QueryCouponDto
import gogo.gogostage.domain.coupon.root.application.dto.UseCouponDto

interface CouponService {
    fun query(couponId: String): QueryCouponDto
    fun use(couponId: String): UseCouponDto
}
