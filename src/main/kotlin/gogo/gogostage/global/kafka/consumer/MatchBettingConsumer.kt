package gogo.gogostage.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogostage.domain.stage.participant.root.application.ParticipateProcessor
import gogo.gogostage.global.kafka.consumer.dto.MatchBettingEvent
import gogo.gogostage.global.kafka.consumer.dto.MatchBettingFailedEvent
import gogo.gogostage.global.kafka.properties.KafkaTopics.MATCH_BETTING
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.util.*

@Service
class MatchBettingConsumer(
    private val objectMapper: ObjectMapper,
    private val participateProcessor: ParticipateProcessor,
    private val stagePublisher: StagePublisher
) : AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @KafkaListener(
        topics = [MATCH_BETTING],
        groupId = "gogo",
        containerFactory = "matchBettingEventListenerContainerFactory"
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        val (key, event) = data.key() to objectMapper.readValue(data.value(), MatchBettingEvent::class.java)
        log.info("${MATCH_BETTING}_topic, key: $key, event: $event")

        try {

            participateProcessor.matchBetting(
                matchId = event.matchId,
                studentId = event.studentId,
                predictedWinTeamId = event.predictedWinTeamId,
                point = event.bettingPoint
            )

        } catch (e: Exception) {
            log.error("Failed to Match Betting metch id  = ${event.matchId}, student id = ${event.studentId}")

            stagePublisher.publishMatchBettingFailedEvent(
                MatchBettingFailedEvent(
                    id = UUID.randomUUID().toString(),
                    studentId = event.studentId,
                    bettingId = event.bettingId,
                    bettingPoint = event.bettingPoint,
                )
            )
        }

        acknowledgment!!.acknowledge()
    }

}
