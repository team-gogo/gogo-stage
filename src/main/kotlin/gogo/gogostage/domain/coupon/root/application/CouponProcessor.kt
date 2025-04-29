package gogo.gogostage.domain.coupon.root.application

import gogo.gogostage.domain.coupon.result.persistence.CouponResult
import gogo.gogostage.domain.coupon.result.persistence.CouponResultRepository
import gogo.gogostage.domain.coupon.root.persistence.Coupon
import gogo.gogostage.domain.coupon.root.persistence.CouponRepository
import gogo.gogostage.domain.coupon.root.persistence.CouponType
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipant
import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.global.error.StageException
import gogo.gogostage.global.internal.student.stub.StudentByIdStub
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class CouponProcessor(
    private val couponRepository: CouponRepository,
    private val couponResultRepository: CouponResultRepository,
    private val stageParticipantRepository: StageParticipantRepository
) {

    fun use(student: StudentByIdStub, coupon: Coupon): CouponResult {
        coupon.used()
        couponRepository.save(coupon)

        val isGain =
            if (coupon.type == CouponType.RANDOM) Random.nextBoolean()
            else true

        val stageParticipant = getStageParticipant(coupon, student)

        val beforePoint = stageParticipant.point
        updatePoints(stageParticipant, coupon, isGain)
        stageParticipantRepository.save(stageParticipant)
        val afterPoint = stageParticipant.point

        val couponResult = CouponResult.of(
            coupon = coupon,
            studentId = student.studentId,
            isGain = isGain,
            beforePoint = beforePoint,
            afterPoint = afterPoint
        )
        return couponResultRepository.save(couponResult)
    }

    private fun getStageParticipant(coupon: Coupon, student: StudentByIdStub) =
        stageParticipantRepository.queryStageParticipantByStageIdAndStudentId(coupon.stage.id, student.studentId)
            ?: throw StageException(
                "Stage Participant Not Found, Stage Id = ${coupon.stage.id}, Student Id = ${student.studentId}",
                HttpStatus.NOT_FOUND.value()
            )

    private fun updatePoints(participant: StageParticipant, coupon: Coupon, isGain: Boolean) {
        if (isGain) {
            participant.plusPoint(coupon.earnPoint)
        } else {
            participant.minusPointMust(coupon.lostPoint!!)
        }
    }

}
