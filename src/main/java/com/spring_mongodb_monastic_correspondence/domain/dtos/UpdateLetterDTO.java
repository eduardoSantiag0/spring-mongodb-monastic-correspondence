package com.spring_mongodb_monastic_correspondence.domain.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.el.stream.Optional;

public record UpdateLetterDTO(
        @Schema(description = "New letter state (optional)", example = "IN_REVIEW", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String new_state,

        @Schema(description = "New content (optional)", example = "New content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String new_content
) {
}
