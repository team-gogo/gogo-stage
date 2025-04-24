package gogo.gogostage.domain.coupon.root.presentation

import gogo.gogostage.domain.coupon.root.application.CouponService
import gogo.gogostage.domain.coupon.root.application.dto.QueryCouponDto
import gogo.gogostage.domain.coupon.root.application.dto.UseCouponDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stage")
class CouponController(
    private val couponService: CouponService,
) {

    @GetMapping("/coupon")
    fun query(
        @RequestParam couponId: String,
    ): ResponseEntity<QueryCouponDto> {
        val response = couponService.query(couponId)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/coupon")
    fun use(
        @RequestParam couponId: String,
    ): ResponseEntity<UseCouponDto> {
        val response = couponService.use(couponId)
        return ResponseEntity.ok(response)
    }

}
