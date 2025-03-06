package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.CreateStageRuleDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StageServiceImpl : StageService {

    @Transactional
    override fun createFast(dto: CreateStageRuleDto) {
        TODO("Not yet implemented")
    }

}
