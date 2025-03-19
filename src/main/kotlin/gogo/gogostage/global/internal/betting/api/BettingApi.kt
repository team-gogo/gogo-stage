package gogo.gogostage.global.internal.betting.api

import gogo.gogostage.global.internal.betting.stub.BettingBundleDto

interface BettingApi {
    fun bundle(matchIds: List<Long>, studentId: Long): BettingBundleDto
}
