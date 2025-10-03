package com.spring_mongodb_monastic_correspondence.domain.model;

import org.springframework.data.annotation.Id;

public abstract class Letter {
    @Id
    protected String id;
    protected String sender;
    protected String receiver;
    protected String content;
    protected int  approximateYear;
    protected State currentState;
    protected int version;

    public Letter(String id, String sender, String receiver, String content, int approximateYear, State currentState, int version) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.approximateYear = approximateYear;
        this.currentState = currentState;
        this.version = version;
    }

    public Letter(String sender, String receiver, String content, int approximateYear, State currentState, int version) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.approximateYear = approximateYear;
        this.currentState = currentState;
        this.version = version;
    }

    public Letter() {

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

    public int getVersion() {
        return version;
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

    public void setApproximateYear(int approximateYear) {
        this.approximateYear = approximateYear;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
