package gogo.gogostage.domain.coupon.root.application.dto

import gogo.gogostage.domain.coupon.root.persistence.CouponType

data class QueryCouponDto(
    val isUsed: Boolean,
    val stageId: Long?,
    val stageName: String?,
    val couponType: CouponType,
    val point: Long?
)

data class UseCouponDto(
    val isGain: Boolean,
    val earnedPoint: Long?,
    val lostedPoint: Long?,
    val beforePoint: Long,
    val afterPoint: Long,
)
