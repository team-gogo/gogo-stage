package gogo.gogostage.domain.coupon.root.application

import gogo.gogostage.domain.coupon.result.persistence.CouponResult
import gogo.gogostage.domain.coupon.root.application.dto.QueryCouponDto
import gogo.gogostage.domain.coupon.root.application.dto.UseCouponDto
import gogo.gogostage.domain.coupon.root.persistence.Coupon
import gogo.gogostage.domain.coupon.root.persistence.CouponType
import org.springframework.stereotype.Component

@Component
class CouponMapper {

    fun map(coupon: Coupon) = QueryCouponDto(
        isUsed = coupon.isUsed,
        stageId = coupon.stage.id,
        stageName = coupon.stage.name,
        couponType = coupon.type,
        point = if (coupon.type == CouponType.NORMAL) coupon.earnPoint else null,
    )

    fun mapResult(coupon: Coupon, couponResult: CouponResult) = UseCouponDto(
        isGain = couponResult.isGain,
        earnedPoint = if (couponResult.isGain) coupon.earnPoint else null,
        lostedPoint = if (couponResult.isGain.not()) coupon.lostPoint else null,
        beforePoint = couponResult.beforePoint,
        afterPoint = couponResult.afterPoint,
    )

}
