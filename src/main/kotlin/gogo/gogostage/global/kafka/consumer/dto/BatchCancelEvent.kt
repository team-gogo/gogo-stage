package gogo.gogostage.global.kafka.consumer.dto

data class BatchCancelEvent(
    val id: String,
    val batchId: Long,
    val matchId: Long,
)
