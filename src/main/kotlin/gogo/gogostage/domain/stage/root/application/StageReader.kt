package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.StageParticipantPointRankDto
import gogo.gogostage.domain.stage.root.persistence.Stage
import gogo.gogostage.domain.stage.root.persistence.StageRepository
import gogo.gogostage.global.error.StageException
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class StageReader(
    private val stageRepository: StageRepository
) {

    fun read(stageId: Long) =
        stageRepository.findByIdOrNull(stageId)
            ?: throw StageException("Stage Not Found, stageId = $stageId", HttpStatus.NOT_FOUND.value())
    fun readAllBySchoolId(schoolId: Long): List<Stage> = stageRepository.findAllBySchoolId(schoolId)

    fun readPointRank(stage: Stage, page: Int, size: Int): StageParticipantPointRankDto {
        val pageRequest = PageRequest.of(page, size)
        return stageRepository.queryPointRank(stage, pageRequest, size)
    }
    fun readMy(studentId: Long): List<Stage> = stageRepository.findMy(studentId)

}