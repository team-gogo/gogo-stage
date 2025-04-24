package gogo.gogostage.domain.coupon.root.application

import gogo.gogostage.domain.coupon.root.application.dto.QueryCouponDto
import gogo.gogostage.domain.coupon.root.application.dto.UseCouponDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponServiceImpl : CouponService {

    @Transactional(readOnly = true)
    override fun query(couponId: String): QueryCouponDto {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun use(couponId: String): UseCouponDto {
        TODO("Not yet implemented")
    }

}
