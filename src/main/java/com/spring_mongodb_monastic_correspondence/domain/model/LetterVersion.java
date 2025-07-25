package com.spring_mongodb_monastic_correspondence.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "letter_versions")
public class LetterVersion extends Letter{

    @Field("original_id")
    private String originalId;

    public LetterVersion() {
        super();
    }

    public LetterVersion(LettersEntity entity) {
        super(entity.id, entity.sender, entity.receiver, entity.content, entity.approximateYear, entity.currentState, entity.version);
        this.originalId = entity.id;
    }

    public String getLetter_id() {
        return originalId;
    }

    public void setLetter_id(String originalId) {
        this.originalId = originalId;
    }
}
