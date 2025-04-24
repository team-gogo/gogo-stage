package gogo.gogostage.domain.coupon.root.application

import gogo.gogostage.domain.coupon.root.application.dto.QueryCouponDto
import gogo.gogostage.domain.coupon.root.application.dto.UseCouponDto
import gogo.gogostage.domain.stage.root.application.StageValidator
import gogo.gogostage.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponServiceImpl(
    private val userUtil: UserContextUtil,
    private val stageValidator: StageValidator,
    private val couponReader: CouponReader,
    private val couponValidator: CouponValidator,
    private val couponMapper: CouponMapper,
    private val couponProcessor: CouponProcessor,
) : CouponService {

    @Transactional(readOnly = true)
    override fun query(couponId: String): QueryCouponDto {
        val student = userUtil.getCurrentStudent()
        val coupon = couponReader.read(couponId)
        stageValidator.validStage(student, coupon.stage.id)
        return couponMapper.map(coupon)
    }

    @Transactional
    override fun use(couponId: String): UseCouponDto {
        val student = userUtil.getCurrentStudent()
        val coupon = couponReader.readForUpdate(couponId)
        couponValidator.valid(coupon)
        stageValidator.validStage(student, coupon.stage.id)
        val couponResult = couponProcessor.use(student, coupon)
        return couponMapper.mapResult(coupon, couponResult)
    }

}
