package gogo.gogostage.global.internal.user.stub

import com.fasterxml.jackson.annotation.JsonProperty

enum class Sex {
    @JsonProperty("MALE")
    MALE,
    @JsonProperty("FEMALE")
    FEMALE
}