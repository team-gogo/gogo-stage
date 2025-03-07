package gogo.gogostage.global.kafka.consumer.dto

data class BatchCancelDeleteTempPointFailedEvent(
    val id: String,
    val batchId: Long
)
