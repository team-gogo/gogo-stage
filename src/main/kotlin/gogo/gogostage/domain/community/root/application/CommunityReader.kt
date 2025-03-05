package gogo.gogostage.domain.community.root.application

import gogo.gogostage.domain.community.root.persistence.CommunityRepository
import gogo.gogostage.domain.game.application.GameReader
import gogo.gogostage.global.error.StageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CommunityReader(
    private val gameReader: GameReader,
    private val communityRepository: CommunityRepository
) {

    fun readCommunityByGameId(gameId: Long) =
        communityRepository.findByGameId(gameId)
            ?: throw StageException("Community Not Found, gameId: $gameId", HttpStatus.NOT_FOUND.value())


}