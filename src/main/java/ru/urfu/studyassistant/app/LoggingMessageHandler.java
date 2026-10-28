package ru.urfu.studyassistant.app;

import ru.urfu.studyassistant.core.port.Message;
import ru.urfu.studyassistant.core.port.MessageHandler;

public class LoggingMessageHandler implements MessageHandler {

    private final MessageHandler delegate;

    public LoggingMessageHandler(MessageHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public String onMessage(Message message) {
        System.out.println("[VK] <- " + message.userId().value() + ": " + message.text());
        String reply = delegate.onMessage(message);
        System.out.println("[VK] -> " + message.userId().value() + ": " + reply);
        return reply;
    }
}