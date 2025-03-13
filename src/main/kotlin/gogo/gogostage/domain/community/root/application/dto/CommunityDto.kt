package gogo.gogostage.domain.community.root.application.dto

import gogo.gogostage.domain.game.persistence.GameCategory
import org.jetbrains.annotations.NotNull

data class WriteCommunityBoardDto(
    @NotNull
    val title: String,
    @NotNull
    val content: String,
    @NotNull
    val gameCategory: GameCategory
)