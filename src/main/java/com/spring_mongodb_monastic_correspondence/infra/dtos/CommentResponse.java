package com.spring_mongodb_monastic_correspondence.infra.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        String letterId,
        String author,
        String content,
        LocalDateTime createdAt,
        int versionCommented
) {
}
