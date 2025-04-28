package gogo.gogostage.domain.community.root.event.handler

import gogo.gogostage.domain.community.root.application.CommunityProcessor
import gogo.gogostage.domain.community.root.event.BoardViewEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class BoardViewEventHandler(
    private val communityProcessor: CommunityProcessor,
) {

    @Async("asyncExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun eventHandler(event: BoardViewEvent) {
        communityProcessor.saveBoardView(event)
    }

}