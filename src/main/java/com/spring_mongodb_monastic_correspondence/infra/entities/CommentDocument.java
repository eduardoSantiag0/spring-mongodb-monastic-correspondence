package com.spring_mongodb_monastic_correspondence.infra.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("comments")
public class CommentDocument {

    @Id
    private String id;

    private final UUID commentId;
    private final String letterId;
    private final String author;
    private final String content;
    private final LocalDateTime createdAt;
//    private final LocalDateTime createdAt;
    private final int versionCommented;

    public CommentDocument(UUID commentId, String letterId, String author, String content, LocalDateTime createdAt, int versionCommented) {
        this.commentId = commentId;
        this.letterId = letterId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.versionCommented = versionCommented;
    }

    public String getId() {
        return id;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public String getLetterId() {
        return letterId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getVersionCommented() {
        return versionCommented;
    }
}
