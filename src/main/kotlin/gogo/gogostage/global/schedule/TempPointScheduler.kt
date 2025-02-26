package gogo.gogostage.global.schedule

import gogo.gogostage.domain.stage.participant.root.persistence.StageParticipantRepository
import gogo.gogostage.domain.stage.participant.temppoint.persistence.TempPointRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class TempPointScheduler(
    private val tempPointRepository: TempPointRepository,
    private val stageParticipantRepository: StageParticipantRepository
) {

    @Transactional
    @Scheduled(fixedRate = 30000)
    fun applyExpiredTempPoints() {
        val now = LocalDateTime.now()
        val expiredTempPoints = tempPointRepository.findExpiredTempPoints(now)

        expiredTempPoints.forEach { tempPoint ->
            val participant = tempPoint.stageParticipant
            participant.plusPoint(tempPoint.tempPoint)
            tempPoint.applied()
        }

        stageParticipantRepository.saveAll(expiredTempPoints.map { it.stageParticipant })
        tempPointRepository.saveAll(expiredTempPoints)
    }

}
