package gogo.gogostage.global.internal.betting.api

import gogo.gogostage.global.feign.client.BettingClient
import gogo.gogostage.global.internal.betting.stub.BettingBundleDto
import org.springframework.stereotype.Component

@Component
class BettingApiImpl(
    private val bettingClient: BettingClient,
) : BettingApi {

    override fun bundle(matchIds: List<Long>, studentId: Long): BettingBundleDto =
        bettingClient.bundle(matchIds, studentId)

}
