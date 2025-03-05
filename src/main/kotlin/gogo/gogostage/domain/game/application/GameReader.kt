package gogo.gogostage.domain.game.application

import gogo.gogostage.domain.game.persistence.GameRepository
import gogo.gogostage.global.error.StageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class GameReader(
    private val gameRepository: GameRepository
) {

    fun readGame(gameId: Long) =
        gameRepository.findByIdOrNull(gameId)
            ?: throw StageException("Game Not Found, gameId = $gameId", HttpStatus.NOT_FOUND.value())

}