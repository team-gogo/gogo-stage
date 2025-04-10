package gogo.gogostage.domain.stage.root.application

import gogo.gogostage.domain.stage.root.application.dto.*

interface StageService {
    fun createFast(dto: CreateFastStageDto)
    fun createOfficial(dto: CreateOfficialStageDto)
    fun join(stageId: Long, dto: StageJoinDto)
    fun confirm(stageId: Long, dto: StageConfirmDto)
    fun queryAll(): QueryStageDto
    fun me(): QueryMyStageDto
    fun getPointRank(stageId: Long, page: Int, size: Int): StageParticipantPointRankDto
    fun checkMeStageMaintainer(stageId: Long): CheckStageMaintainerDto
    fun wasted(stageId: Long): QueryMyWastedDto
}
