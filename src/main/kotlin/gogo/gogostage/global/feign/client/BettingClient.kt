package gogo.gogostage.global.feign.client

import gogo.gogostage.global.feign.fallback.BettingClientFallbackFactory
import gogo.gogostage.global.internal.betting.stub.BettingBundleDto
import gogo.gogostage.global.internal.betting.stub.TotalBettingPointDto
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "gogo-betting", fallbackFactory = BettingClientFallbackFactory::class)
interface BettingClient {
    @GetMapping("/betting/bundle")
    fun bundle(
        @RequestParam("matchIds") matchIds: List<Long>,
        @RequestParam("studentId") studentId: Long
    ): BettingBundleDto

    @GetMapping("/betting/point")
    fun totalBettingPoint(
        @RequestParam("matchIds") matchIds: List<Long>,
        @RequestParam("studentId") studentId: Long
    ): TotalBettingPointDto
}
