package ru.urfu.studyassistant.infrastructure.console;

import ru.urfu.studyassistant.core.port.ChatPlatform;
import ru.urfu.studyassistant.core.port.MessageHandler;

import java.util.Scanner;

public class ConsoleChatPlatform implements ChatPlatform {

    private final Scanner scanner;
    private MessageHandler handler;

    public ConsoleChatPlatform(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void send(String text) {
        System.out.println(text);
    }

    @Override
    public void run() {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            handler.onMessage(line);
            if ("/exit".equals(line.trim())) {
                break;
            }
        }
    }
}