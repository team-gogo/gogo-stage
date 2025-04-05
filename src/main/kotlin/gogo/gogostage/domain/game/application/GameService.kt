package gogo.gogostage.domain.game.application

import gogo.gogostage.domain.game.application.dto.QueryGameDto
import gogo.gogostage.domain.game.application.dto.QueryGameFormatDto

interface GameService {
    fun queryAll(stageId: Long): QueryGameDto
    fun queryFormat(gameId: Long): QueryGameFormatDto
}
