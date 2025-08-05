package com.spring_mongodb_monastic_correspondence.domain.dtos;

import com.spring_mongodb_monastic_correspondence.domain.model.State;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateLetterDTO(
        @Schema(example = "Father James", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String sender,

        @Schema(example = "Father Kornelius", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String receiver,

        @Schema(example = "I discovered a sealed chamber with strange plastic-bound texts referencing a deity called Wi-Fi...")
        @NotBlank String content,

        @Schema(example = "1860", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Integer approximateYear,

        @Schema(example = "READABLE", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull State currentState
) {}
