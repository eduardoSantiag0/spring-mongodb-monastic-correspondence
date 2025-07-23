package com.spring_mongodb_monastic_correspondence.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "letters")
public class LettersEntity {

    @Id
    private String id;
    private String sender;
    private String receiver;
    private String content;
    private int  approximateYear;
    private State currentState;

    public LettersEntity() {
    }

    public LettersEntity(String id, String sender, String receiver, String content, int approximateYear, State currentState) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.approximateYear = approximateYear;
        this.currentState = currentState;
    }

    public LettersEntity(String sender, String receiver, String content, int approximateYear, State currrentState) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.approximateYear = approximateYear;
        this.currentState = currrentState;
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


    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public int getApproximateYear() {
        return approximateYear;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(int date) {
        this.approximateYear = approximateYear;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
