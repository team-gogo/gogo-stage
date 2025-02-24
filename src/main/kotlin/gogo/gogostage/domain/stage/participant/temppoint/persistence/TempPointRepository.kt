package gogo.gogostage.domain.stage.participant.temppoint.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface TempPointRepository: JpaRepository<TempPoint, Long> {
}
