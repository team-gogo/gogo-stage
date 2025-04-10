package gogo.gogostage.domain.game.application

import gogo.gogostage.domain.game.persistence.Game
import gogo.gogostage.domain.game.persistence.GameSystem
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class GameValidator {

    fun validFormat(game: Game) {
        if (game.system != GameSystem.TOURNAMENT) {
            throw StageException("해당 경기의 시스템이 토너먼트가 아닙니다.", HttpStatus.BAD_REQUEST.value())
        }
    }

}
