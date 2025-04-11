package gogo.gogostage.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogostage.domain.community.root.application.CommunityProcessor
import gogo.gogostage.global.kafka.consumer.dto.AiCommentFilterEvent
import gogo.gogostage.global.kafka.properties.KafkaTopics.AI_COMMENT_FILTER
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class AiCommentFilterConsumer(
    private val objectMapper: ObjectMapper,
    private val communityProcessor: CommunityProcessor
): AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @KafkaListener(
        topics = [AI_COMMENT_FILTER],
        groupId = "gogo",
        containerFactory = "aiCommentFilterEventListenerContainerFactory"
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        val (key, event) = data.key() to objectMapper.readValue(data.value(), AiCommentFilterEvent::class.java)
        log.info("${AI_COMMENT_FILTER}_topic, key: $key, event: $event")

        try {

            communityProcessor.commentFilteredTrue(event.commentId)

        } catch (e: Exception) {
            log.error("Failed to Batch Addition Temp Point, Batch Id = ${event.id}", e)
        }

        acknowledgment!!.acknowledge()
    }

}