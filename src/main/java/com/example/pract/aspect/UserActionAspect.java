package com.example.pract.aspect;

import com.example.pract.controller.MainController;
import com.example.pract.events.UserActionEvent;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserActionAspect {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final ApplicationEventPublisher eventPublisher;

    public UserActionAspect(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @After("execution(* com.example.pract.*.*(..))")
    public void logUserAction() {
        log.debug("Intercepted method call, publishing UserActionEvent");
        System.out.println("UserActionAspect.logUserAction() called");
        eventPublisher.publishEvent(new UserActionEvent(this, "User action performed"));
    }
}
