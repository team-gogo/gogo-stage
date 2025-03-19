package gogo.gogostage.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogostage.domain.match.root.application.MatchProcessor
import gogo.gogostage.domain.stage.participant.temppoint.application.TempPointProcessor
import gogo.gogostage.global.kafka.consumer.dto.BatchCancelDeleteTempPointFailedEvent
import gogo.gogostage.global.kafka.consumer.dto.BatchCancelEvent
import gogo.gogostage.global.kafka.properties.KafkaTopics.BATCH_CANCEL
import gogo.gogostage.global.kafka.publisher.StagePublisher
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.util.*

@Service
class BatchCancelConsumer(
    private val objectMapper: ObjectMapper,
    private val stagePublisher: StagePublisher,
    private val matchProcessor: MatchProcessor
) : AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @KafkaListener(
        topics = [BATCH_CANCEL],
        groupId = "gogo",
        containerFactory = "batchCancelEventListenerContainerFactory"
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        val (key, event) = data.key() to objectMapper.readValue(data.value(), BatchCancelEvent::class.java)
        log.info("${BATCH_CANCEL}_topic, key: $key, event: $event")

        try {

            matchProcessor.cancelBatch(event)

        } catch (e: Exception) {
            log.error("Failed to Batch Addition Temp Point, Batch Id = ${event.batchId}", e)

            stagePublisher.publishBatchCancelDeleteTempPointFailedEvent(
                BatchCancelDeleteTempPointFailedEvent(
                    id = UUID.randomUUID().toString(),
                    batchId = event.batchId,
                )
            )
        }

        acknowledgment!!.acknowledge()
    }

}
