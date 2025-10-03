package com.spring_mongodb_monastic_correspondence.infra.entities;

import com.spring_mongodb_monastic_correspondence.domain.model.EmbeddedComment;
import com.spring_mongodb_monastic_correspondence.domain.model.Letter;
import com.spring_mongodb_monastic_correspondence.domain.model.State;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "letters")
public class LettersEntity extends Letter {

    private int commentsCount;
    private LocalDateTime lastCommentAt;
    private EmbeddedComment lastComment;

    public LettersEntity() {
    }

    public LettersEntity(String id, String sender, String receiver, String content, int approximateYear, State currentState, int version) {
        super(id, sender, receiver, content, approximateYear, currentState, version);
    }

    public LettersEntity(String sender, String receiver, String content, int approximateYear, State currentState, int version) {
        super(sender, receiver, content, approximateYear, currentState, version);
    }

    @Override
    public String toString() {
        return "LettersEntity{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                ", date=" + approximateYear +
                ", currentState=" + currentState +
                '}';
    }

    public void incVersion() {
        this.version++;
    }

    public EmbeddedComment getLastComment() {
        return lastComment;
    }

    public void addComment(EmbeddedComment embeddedComment) {
        this.lastComment = embeddedComment;
        commentsCount++;
        this.lastCommentAt = embeddedComment.createdAt();
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public LocalDateTime getLastCommentAt() {
        return lastCommentAt;
    }

}
