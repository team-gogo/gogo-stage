package gogo.gogostage.global.internal.betting.api

import gogo.gogostage.global.internal.betting.stub.BettingBundleDto
import gogo.gogostage.global.internal.betting.stub.TotalBettingPointDto

interface BettingApi {
    fun bundle(matchIds: List<Long>, studentId: Long): BettingBundleDto
    fun totalBettingPoint(matchIds: List<Long>, studentId: Long): TotalBettingPointDto
}
