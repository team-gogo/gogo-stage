package gogo.gogostage.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogostage.domain.stage.participant.root.application.ParticipateProcessor
import gogo.gogostage.global.kafka.consumer.dto.*
import gogo.gogostage.global.kafka.properties.KafkaTopics.TICKET_ADDITION_FAILED
import gogo.gogostage.global.kafka.properties.KafkaTopics.TICKET_SHOP_BUY
import gogo.gogostage.global.kafka.publisher.StagePublisher
import gogo.gogostage.global.saga.TicketAdditionFailedSaga
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.util.*

@Service
class TicketAdditionFailedConsumer(
    private val objectMapper: ObjectMapper,
    private val ticketAdditionFailedSaga: TicketAdditionFailedSaga
) : AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @KafkaListener(
        topics = [TICKET_ADDITION_FAILED],
        groupId = "gogo",
        containerFactory = "ticketAdditionFailedEventListenerContainerFactory"
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        val (key, event) = data.key() to objectMapper.readValue(data.value(), TicketAdditionFailedEvent::class.java)
        log.info("${TICKET_ADDITION_FAILED}_topic, key: $key, event: $event")

        ticketAdditionFailedSaga.rollbackPoint(event)

        acknowledgment!!.acknowledge()
    }

}
