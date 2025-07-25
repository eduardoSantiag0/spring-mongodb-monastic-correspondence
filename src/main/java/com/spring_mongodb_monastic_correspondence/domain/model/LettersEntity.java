package com.spring_mongodb_monastic_correspondence.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "letters")
public class LettersEntity extends Letter {

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

}
