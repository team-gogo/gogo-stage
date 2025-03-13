package gogo.gogostage.global.kafka.consumer.dto

data class TicketShopBuyEvent(
    val id: String,
    val stageId: Long,
    val studentId: Long,
    val shopId: Long,
    val shopMiniGameId: Long,
    val ticketType: TicketType,
    val shopReceiptId: Long,
    val ticketPrice: Long,
    val purchaseQuantity: Int,
)

enum class TicketType {
    COINTOSS, YAVARWEE, PLINKO
}
