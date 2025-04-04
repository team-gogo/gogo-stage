package gogo.gogostage.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogostage.domain.match.root.application.MatchProcessor
import gogo.gogostage.global.kafka.consumer.dto.BatchAdditionTempPointFailedEvent
import gogo.gogostage.global.kafka.consumer.dto.MatchBatchEvent
import gogo.gogostage.global.kafka.properties.KafkaTopics.MATCH_BATCH
import gogo.gogostage.global.kafka.publisher.StagePublisher
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.util.*

@Service
class MatchBatchConsumer(
    private val objectMapper: ObjectMapper,
    private val stagePublisher: StagePublisher,
    private val matchProcessor: MatchProcessor
) : AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @KafkaListener(
        topics = [MATCH_BATCH],
        groupId = "gogo",
        containerFactory = "batchCancelEventListenerContainerFactory"
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        val (key, event) = data.key() to objectMapper.readValue(data.value(), MatchBatchEvent::class.java)
        log.info("${MATCH_BATCH}_topic, key: $key, event: $event")

        try {

            matchProcessor.batch(event)

        } catch (e: Exception) {
            log.error("Failed to Cancelled Batch Delete Temp Point, Batch Id = ${event.batchId}", e)

            stagePublisher.publishBatchAdditionTempPointFailedEvent(
                BatchAdditionTempPointFailedEvent(
                    id = UUID.randomUUID().toString(),
                    batchId = event.batchId,
                )
            )
        }

        acknowledgment!!.acknowledge()
    }

}
