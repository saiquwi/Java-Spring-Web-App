package com.example.pract.listeners;

import com.example.pract.events.UserActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserActionEventListener {

    //private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @EventListener
    public void handleUserActionEvent(UserActionEvent event) {
        System.out.println("Received UserActionEvent - " + event.getMessage());
    }
}
