package gogo.gogostage.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogostage.domain.stage.participant.root.application.ParticipateProcessor
import gogo.gogostage.global.kafka.consumer.dto.*
import gogo.gogostage.global.kafka.properties.KafkaTopics.TICKET_SHOP_BUY
import gogo.gogostage.global.kafka.publisher.StagePublisher
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.util.*

@Service
class TicketShopBuyConsumer(
    private val objectMapper: ObjectMapper,
    private val stageParticipateProcessor: ParticipateProcessor,
    private val stagePublisher: StagePublisher
) : AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @KafkaListener(
        topics = [TICKET_SHOP_BUY],
        groupId = "gogo",
        containerFactory = "ticketShopBuyEventListenerContainerFactory"
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        val (key, event) = data.key() to objectMapper.readValue(data.value(), TicketShopBuyEvent::class.java)
        log.info("${TICKET_SHOP_BUY}_topic, key: $key, event: $event")

        try {

            stageParticipateProcessor.buyTicket(event)

        } catch (e: Exception) {

            log.error("Failed to Ticket Shop Buy, Shop Receipt Id = ${event.shopReceiptId}", e)

            stagePublisher.publishTicketPointMinusFailedEvent(
                event = TicketPointMinusFailedEvent(
                    id = UUID.randomUUID().toString(),
                    stageId = event.stageId,
                    shopMiniGameId = event.shopMiniGameId,
                    ticketType = event.ticketType,
                    shopReceiptId = event.shopReceiptId,
                )
            )

        }

        acknowledgment!!.acknowledge()
    }

}
