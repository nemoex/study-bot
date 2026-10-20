package ru.urfu.studyassistant.core.dialog;

import ru.urfu.studyassistant.core.port.Message;
import ru.urfu.studyassistant.core.port.MessageHandler;

import java.util.List;

public class BotEngine implements MessageHandler {

    private final List<Command> commands;

    public BotEngine(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String onMessage(Message message) {
        for (Command command : commands) {
            if (command.matches(message.text())) {
                return command.execute(message.userId(), message.text());
            }
        }
        return "Не понял команду. Напишите /help, чтобы увидеть список команд.";
    }
}