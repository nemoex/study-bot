package ru.urfu.studyassistant.core.port;

public interface ChatPlatform {
    void setHandler(MessageHandler handler);
    void send(String text);
    void run();
}