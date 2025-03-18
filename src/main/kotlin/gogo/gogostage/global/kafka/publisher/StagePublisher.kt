package gogo.gogostage.global.kafka.publisher

import gogo.gogostage.domain.stage.participant.root.event.TicketPointMinusEvent
import gogo.gogostage.domain.stage.root.event.CreateStageFastEvent
import gogo.gogostage.domain.stage.root.event.CreateStageOfficialEvent
import gogo.gogostage.domain.stage.root.event.StageConfirmEvent
import gogo.gogostage.global.kafka.consumer.dto.*
import gogo.gogostage.global.kafka.properties.KafkaTopics.BATCH_ADDITION_TEMP_POINT_FAILED
import gogo.gogostage.global.kafka.properties.KafkaTopics.BATCH_CANCEL_DELETE_TEMP_POINT_FAILED
import gogo.gogostage.global.kafka.properties.KafkaTopics.STAGE_CREATE_OFFICIAL
import gogo.gogostage.global.kafka.properties.KafkaTopics.STAGE_CREATE_FAST
import gogo.gogostage.global.kafka.properties.KafkaTopics.MATCH_BETTING_FAILED
import gogo.gogostage.global.kafka.properties.KafkaTopics.MINIGAME_BET_COMPLETED_FAILED
import gogo.gogostage.global.kafka.properties.KafkaTopics.STAGE_CONFIRM
import gogo.gogostage.global.kafka.properties.KafkaTopics.TICKET_POINT_MINUS
import gogo.gogostage.global.kafka.properties.KafkaTopics.TICKET_POINT_MINUS_FAILED
import gogo.gogostage.global.publisher.TransactionEventPublisher
import org.springframework.stereotype.Component
import java.util.*

@Component
class StagePublisher(
    private val transactionEventPublisher: TransactionEventPublisher
){

    fun publishMatchBettingFailedEvent(
        event: MatchBettingFailedEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = MATCH_BETTING_FAILED,
            key = key,
            event = event
        )
    }

    fun publishBatchAdditionTempPointFailedEvent(
        event: BatchAdditionTempPointFailedEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = BATCH_ADDITION_TEMP_POINT_FAILED,
            key = key,
            event = event
        )
    }

    fun publishBatchCancelDeleteTempPointFailedEvent(
        event: BatchCancelDeleteTempPointFailedEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = BATCH_CANCEL_DELETE_TEMP_POINT_FAILED,
            key = key,
            event = event
        )
    }

    fun publishCreateStageFastEvent(
        event: CreateStageFastEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = STAGE_CREATE_FAST,
            key = key,
            event = event
        )
    }

    fun publishCreateStageOfficialEvent(
        event: CreateStageOfficialEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = STAGE_CREATE_OFFICIAL,
            key = key,
            event = event
        )
    }

    fun publishStageConfirmEvent(
        event: StageConfirmEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = STAGE_CONFIRM,
            key = key,
            event = event
        )
    }

    fun publishTicketPointMinusEvent(
        event: TicketPointMinusEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = TICKET_POINT_MINUS,
            key = key,
            event = event
        )
    }

    fun publishTicketPointMinusFailedEvent(
        event: TicketPointMinusFailedEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = TICKET_POINT_MINUS_FAILED,
            key = key,
            event = event
        )
    }

    fun publishMiniGameBetCompletedFailedEvent(
        event: MiniGameBetCompletedFailedEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = MINIGAME_BET_COMPLETED_FAILED,
            key = key,
            event = event
        )
    }

}
