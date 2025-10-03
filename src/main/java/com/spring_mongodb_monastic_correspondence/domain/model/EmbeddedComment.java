package com.spring_mongodb_monastic_correspondence.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EmbeddedComment(
    UUID commentId,
    String author,
    String content,
    LocalDateTime createdAt,
    int versionCommented

) {
}
