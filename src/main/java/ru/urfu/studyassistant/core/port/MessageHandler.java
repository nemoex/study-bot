package ru.urfu.studyassistant.core.port;

public interface MessageHandler {
    String onMessage(Message message);
}