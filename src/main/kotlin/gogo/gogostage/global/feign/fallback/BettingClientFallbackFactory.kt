package gogo.gogostage.global.feign.fallback

import gogo.gogostage.global.feign.client.BettingClient
import gogo.gogostage.global.filter.LoggingFilter
import gogo.gogostage.global.internal.betting.stub.BettingBundleDto
import gogo.gogostage.global.internal.betting.stub.TotalBettingPointDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.stereotype.Component

@Component
class BettingClientFallbackFactory : FallbackFactory<BettingClient> {

    private val log: Logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun create(cause: Throwable): BettingClient {
        return object : BettingClient {
            override fun bundle(matchIds: List<Long>, studentId: Long): BettingBundleDto {
                log.error("BettingClient.bundle FallBack - matchIds: $matchIds, studentId: $studentId")
                return BettingBundleDto(
                    bettings = emptyList(),
                )
            }

            override fun totalBettingPoint(matchIds: List<Long>, studentId: Long): TotalBettingPointDto {
                log.error("BettingClient.totalBettingPoint FallBack - matchIds: $matchIds, studentId: $studentId")
                throw cause
            }
        }
    }
}
