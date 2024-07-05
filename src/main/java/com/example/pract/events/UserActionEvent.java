package com.example.pract.events;

import org.springframework.context.ApplicationEvent;

public class UserActionEvent extends ApplicationEvent {
    private String message;

    public UserActionEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() { return message; }
}
