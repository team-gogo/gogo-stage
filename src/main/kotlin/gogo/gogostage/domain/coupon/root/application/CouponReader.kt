package gogo.gogostage.domain.coupon.root.application

import gogo.gogostage.domain.coupon.root.persistence.Coupon
import gogo.gogostage.domain.coupon.root.persistence.CouponRepository
import gogo.gogostage.global.error.StageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CouponReader(
    private val couponRepository: CouponRepository
) {

    fun read(couponId: String): Coupon =
        couponRepository.findByIdOrNull(couponId)
            ?: throw StageException("쿠폰을 찾을 수 없습니다. coupon id = $couponId", HttpStatus.NOT_FOUND.value())

    fun readForUpdate(couponId: String): Coupon =
        couponRepository.findByIdOrNullForUpdate(couponId)
            ?: throw StageException("쿠폰을 찾을 수 없습니다. coupon id = $couponId", HttpStatus.NOT_FOUND.value())

}
