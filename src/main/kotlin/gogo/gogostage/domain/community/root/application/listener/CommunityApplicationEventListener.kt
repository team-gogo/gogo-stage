package gogo.gogostage.domain.community.root.application.listener

import gogo.gogostage.domain.community.root.event.BoardCreateEvent
import gogo.gogostage.domain.community.root.event.CommentCreateEvent
import gogo.gogostage.global.kafka.publisher.StagePublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CommunityApplicationEventListener(
    private val stagePublisher: StagePublisher,
) {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun boardCreate(event: BoardCreateEvent) {
        with(event) {
            log.info("published create board application event: {}", id)
            stagePublisher.publishBoardCreateEvent(event)
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun commentCreate(event: CommentCreateEvent) {
        with(event) {
            log.info("published create comment application event: {}", id)
            stagePublisher.publishCommentCreateEvent(event)
        }
    }

}