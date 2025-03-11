package gogo.gogostage.global.kafka.consumer.dto

data class TicketPointMinusFailedEvent(
    val id: String,
    val stageId: Long,
    val shopMiniGameId: Long,
    val ticketType: TicketType,
    val shopReceiptId: Long
)
