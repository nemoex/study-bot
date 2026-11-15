package ru.urfu.studyassistant.infrastructure.logging;

import ru.urfu.studyassistant.core.model.UserId;

import java.io.PrintStream;

public class ConsoleBotLogger implements BotLogger {

    private final PrintStream out;
    private final PrintStream err;

    public ConsoleBotLogger() {
        this(System.out, System.err);
    }

    // Только для тестов
    ConsoleBotLogger(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    @Override
    public void info(String message) {
        out.println("[INFO] " + message);
    }

    @Override
    public void error(String message, Throwable cause) {
        err.println("[ERROR] " + message);
        cause.printStackTrace(err);
    }

    @Override
    public void logIncoming(UserId userId, String text) {
        out.println("[BOT IN] <- " + userId.value() + ": " + text);
    }

    @Override
    public void logOutgoing(UserId userId, String text) {
        out.println("[BOT OUT] -> " + userId.value() + ": " + text);
    }
}