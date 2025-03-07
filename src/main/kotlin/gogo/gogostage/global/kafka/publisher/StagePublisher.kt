package gogo.gogostage.global.kafka.publisher

import gogo.gogostage.domain.stage.root.event.CreateStageFastEvent
import gogo.gogostage.global.kafka.consumer.dto.BatchAdditionTempPointFailedEvent
import gogo.gogostage.global.kafka.consumer.dto.BatchCancelDeleteTempPointFailedEvent
import gogo.gogostage.global.kafka.consumer.dto.MatchBettingFailedEvent
import gogo.gogostage.global.kafka.properties.KafkaTopics.BATCH_ADDITION_TEMP_POINT_FAILED
import gogo.gogostage.global.kafka.properties.KafkaTopics.BATCH_CANCEL_DELETE_TEMP_POINT_FAILED
import gogo.gogostage.global.kafka.properties.KafkaTopics.CREATE_STAGE_FAST
import gogo.gogostage.global.kafka.properties.KafkaTopics.MATCH_BETTING_FAILED
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

    fun publishCreateStageFastEVent(
        event: CreateStageFastEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = CREATE_STAGE_FAST,
            key = key,
            event = event
        )
    }

}
