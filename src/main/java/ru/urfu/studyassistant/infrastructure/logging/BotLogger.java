package ru.urfu.studyassistant.infrastructure.logging;

import ru.urfu.studyassistant.core.model.UserId;

public interface BotLogger {

    void info(String message);

    void error(String message, Throwable cause);

    void logIncoming(UserId userId, String text);

    void logOutgoing(UserId userId, String text);
}