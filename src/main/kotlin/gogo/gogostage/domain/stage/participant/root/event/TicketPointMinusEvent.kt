package gogo.gogostage.domain.stage.participant.root.event

import gogo.gogostage.global.kafka.consumer.dto.TicketType

data class TicketPointMinusEvent(
    val id: String,
    val stageId: Long,
    val studentId: Long,
    val shopMiniGameId: Long,
    val ticketType: TicketType,
    val ticketPrice: Long,
    val purchaseQuantity: Int
)
