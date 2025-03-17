package gogo.gogostage.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogostage.domain.stage.participant.root.application.ParticipateProcessor
import gogo.gogostage.global.kafka.consumer.dto.MiniGameBetCompletedEvent
import gogo.gogostage.global.kafka.consumer.dto.MiniGameBetCompletedFailedEvent
import gogo.gogostage.global.kafka.properties.KafkaTopics.MINIGAME_BET_COMPLETED
import gogo.gogostage.global.kafka.publisher.StagePublisher
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.util.*

@Service
class MiniGameBetCompletedConsumer(
    private val objectMapper: ObjectMapper,
    private val stagePublisher: StagePublisher,
    private val stageParticipateProcessor: ParticipateProcessor
) : AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @KafkaListener(
        topics = [MINIGAME_BET_COMPLETED],
        groupId = "gogo",
        containerFactory = "batchCancelEventListenerContainerFactory"
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        val (key, event) = data.key() to objectMapper.readValue(data.value(), MiniGameBetCompletedEvent::class.java)
        log.info("${MINIGAME_BET_COMPLETED}_topic, key: $key, event: $event")

        try {

            stageParticipateProcessor.minigameBetting(event)

        } catch (e: Exception) {
            log.error("Failed to MiniGame Bet Completed, Stage Id = ${event.stageId}, MiniGame Type = ${event.gameType}, Student Id = ${event.studentId}", e)

            stagePublisher.publishMiniGameBetCompletedFailedEvent(
                MiniGameBetCompletedFailedEvent(
                    id = UUID.randomUUID().toString(),
                    stageId = event.stageId,
                    studentId = event.studentId,
                    gameType = event.gameType,
                ),
            )
        }

        acknowledgment!!.acknowledge()
    }

}
