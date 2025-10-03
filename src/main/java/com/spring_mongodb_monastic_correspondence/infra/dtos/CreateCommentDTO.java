package com.spring_mongodb_monastic_correspondence.infra.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(name = "CreateCommentDTO", description = "DTO for creating a new comment")
public record CreateCommentDTO(
        @Schema(description = "Author of the comment", example = "Father John Doe", required = true)
        String author,
        @Schema(description = "Content of the comment", example = "This is a comment.", required = true)
        String content
) {
}
