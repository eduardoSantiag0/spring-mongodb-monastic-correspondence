package com.spring_mongodb_monastic_correspondence.domain.dtos;

import org.apache.el.stream.Optional;

public record UpdateLetterDTO(
        String new_state,
        String new_content
) {
}
