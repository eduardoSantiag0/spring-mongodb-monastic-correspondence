package com.spring_mongodb_monastic_correspondence.infra.dtos;

import com.spring_mongodb_monastic_correspondence.domain.model.State;
import jakarta.validation.constraints.NotBlank;

public record LetterVersionsDTO(
        String id,
        String originalId,
        @NotBlank String sender,
        @NotBlank String receiver,
        @NotBlank String content,
        @NotBlank int approximateYear,
        @NotBlank State currentState,
        int version
) {
}
