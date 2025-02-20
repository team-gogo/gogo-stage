package gogo.gogostage.global.publisher

interface TransactionEventPublisher {
    fun publishEvent(topic: String, key: String, event: Any)
}