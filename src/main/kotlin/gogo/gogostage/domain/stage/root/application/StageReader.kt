package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.persistence.Stage
import gogo.gogostage.domain.stage.root.persistence.StageRepository
import org.springframework.stereotype.Component

@Component
class StageReader(
    private val stageRepository: StageRepository
) {

    fun read(schoolId: Long): List<Stage> = stageRepository.findAllBySchoolId(schoolId)

}
