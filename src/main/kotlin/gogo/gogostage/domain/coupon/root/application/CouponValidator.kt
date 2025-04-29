package gogo.gogostage.domain.coupon.root.application

import gogo.gogostage.domain.coupon.root.persistence.Coupon
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CouponValidator {

    fun valid(coupon: Coupon) {
        val isUsed = coupon.isUsed
        if (isUsed) {
            throw StageException("이미 사용된 쿠폰입니다.", HttpStatus.BAD_REQUEST.value())
        }
    }

}
