package ru.urfu.studyassistant.infrastructure.console;

import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.port.ChatPlatform;
import ru.urfu.studyassistant.core.port.Message;
import ru.urfu.studyassistant.core.port.MessageHandler;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleChatPlatform implements ChatPlatform {

    private final Scanner scanner;
    private final UserId currentUserId;
    private final PrintStream out;
    private MessageHandler handler;

    public ConsoleChatPlatform(Scanner scanner, UserId currentUserId) {
        this(scanner, currentUserId, System.out);
    }

    // Только для тестов
    ConsoleChatPlatform(Scanner scanner, UserId currentUserId, PrintStream out) {
        this.scanner = scanner;
        this.currentUserId = currentUserId;
        this.out = out;
    }

    @Override
    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public void send(String text) {
        out.println(text);
    }

    @Override
    public void run() {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().equals("/exit")) {
                send("До встречи!");
                return;
            }
            send(handler.onMessage(new Message(currentUserId, line)));
        }
    }
}