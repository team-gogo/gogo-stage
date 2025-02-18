package gogo.gogostage.global.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogostage.global.publisher.TransactionEventPublisher
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : TransactionEventPublisher {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    override fun publishEvent(topic: String, key: String, event: Any) {
        log.info("Publishing event to topic: $topic, key: $key, event: $event")
        kafkaTemplate.send(topic, key, objectMapper.writeValueAsString(event))
    }
}
