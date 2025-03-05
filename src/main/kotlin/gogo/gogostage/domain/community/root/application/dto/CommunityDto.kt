package gogo.gogostage.domain.community.root.application.dto

import org.jetbrains.annotations.NotNull

data class WriteCommunityBoardDto(
    @NotNull
    val title: String,
    @NotNull
    val content: String
)