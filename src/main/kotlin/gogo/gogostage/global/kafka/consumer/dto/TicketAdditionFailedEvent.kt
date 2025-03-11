package gogo.gogostage.global.kafka.consumer.dto

data class TicketAdditionFailedEvent(
    val id: String,
    val studentId: Long,
    val stageId: Long,
    val shopMiniGameId: Long,
    val ticketType: TicketType,
    val shopReceiptId: Long,
    val ticketPrice: Long,
    val purchaseQuantity: Int
)
