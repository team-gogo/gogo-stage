package gogo.gogostage.global.kafka.consumer

import gogo.gogostage.global.kafka.consumer.dto.MatchBettingFailedEvent
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

}
