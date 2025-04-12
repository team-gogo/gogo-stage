package gogo.gogostage.global.feign.client

import gogo.gogostage.global.internal.betting.stub.BettingBundleDto
import gogo.gogostage.global.internal.betting.stub.TotalBettingPointDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "gogo-betting")
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
