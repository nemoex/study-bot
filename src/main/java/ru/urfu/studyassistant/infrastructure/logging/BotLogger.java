package ru.urfu.studyassistant.infrastructure.logging;

import ru.urfu.studyassistant.core.model.UserId;

public class BotLogger {
    
    public void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public void error(String message, Throwable t) {
        System.err.println("[ERROR] " + message);
        t.printStackTrace();
    }

    public void logIncoming(UserId userId, String text) {
        System.out.println("[BOT IN] <- " + userId.value() + ": " + text);
    }

    public void logOutgoing(UserId userId, String text) {
        System.out.println("[BOT OUT] -> " + userId.value() + ": " + text);
    }
}