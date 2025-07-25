package com.spring_mongodb_monastic_correspondence.domain.dtos;

import com.spring_mongodb_monastic_correspondence.domain.model.State;

public record LettersDTO(
        String id,
        String sender,
        String receiver,
        String content,
        int approximateYear,
        State currentState,
        int version
) {
}
