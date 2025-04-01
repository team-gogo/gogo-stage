package gogo.gogostage.global.kafka.configuration

import gogo.gogostage.global.kafka.consumer.*
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.listener.AcknowledgingMessageListener

@EnableKafka
@Configuration
class KafkaConsumerConfig(
    private val kafkaProperties: KafkaProperties
) {

    @Bean
    fun miniGameBetCompletedEventListenerContainerFactory(listener: MiniGameBetCompletedConsumer): ConcurrentKafkaListenerContainerFactory<String, String> =
        makeFactory(listener)

    @Bean
    fun ticketAdditionFailedEventListenerContainerFactory(listener: TicketAdditionFailedConsumer): ConcurrentKafkaListenerContainerFactory<String, String> =
        makeFactory(listener)

    @Bean
    fun ticketShopBuyEventListenerContainerFactory(listener: TicketShopBuyConsumer): ConcurrentKafkaListenerContainerFactory<String, String> =
        makeFactory(listener)

    @Bean
    fun matchBettingEventListenerContainerFactory(listener: MatchBettingConsumer): ConcurrentKafkaListenerContainerFactory<String, String> =
        makeFactory(listener)

    @Bean
    fun matchBatchEventListenerContainerFactory(listener: MatchBatchConsumer): ConcurrentKafkaListenerContainerFactory<String, String> =
        makeFactory(listener)

    @Bean
    fun batchCancelEventListenerContainerFactory(listener: BatchCancelConsumer): ConcurrentKafkaListenerContainerFactory<String, String> =
        makeFactory(listener)

    private fun makeFactory(listener: AcknowledgingMessageListener<String, String>): ConcurrentKafkaListenerContainerFactory<String, String> {
        return ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            consumerFactory = consumerFactory()
            containerProperties.ackMode = kafkaProperties.listener.ackMode
            setConcurrency(1)
            containerProperties.messageListener = listener
            containerProperties.pollTimeout = 5000
        }
    }

    private fun consumerFactory(): ConsumerFactory<String, String> {
        return DefaultKafkaConsumerFactory(kafkaProperties.buildConsumerProperties(null))
    }
}
