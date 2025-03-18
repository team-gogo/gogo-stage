package gogo.gogostage.domain.game.application

import gogo.gogostage.domain.game.application.dto.QueryGameDto

interface GameService {
    fun queryAll(stageId: Long): QueryGameDto
}
