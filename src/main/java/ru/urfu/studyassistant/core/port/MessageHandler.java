package ru.urfu.studyassistant.core.port;

public interface MessageHandler {
    void onMessage(String text);
}