package ru.urfu.studyassistant.core.port;

import ru.urfu.studyassistant.core.model.UserId;

public interface ChatPlatform {
    void setHandler(MessageHandler handler);
    void send(UserId to, String text);
    void run();
}