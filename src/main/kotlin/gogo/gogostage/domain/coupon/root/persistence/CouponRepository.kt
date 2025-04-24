package gogo.gogostage.domain.coupon.root.persistence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CouponRepository: CrudRepository<Coupon, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId")
    fun findByIdOrNullForUpdate(couponId: String): Coupon?
}
