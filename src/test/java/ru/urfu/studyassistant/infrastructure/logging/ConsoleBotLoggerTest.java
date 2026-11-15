package ru.urfu.studyassistant.infrastructure.logging;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.UserId;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleBotLoggerTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final ConsoleBotLogger logger = new ConsoleBotLogger(
            new PrintStream(out, true, StandardCharsets.UTF_8),
            new PrintStream(err, true, StandardCharsets.UTF_8)
    );

    private final UserId user = new UserId("u1");

    @Test
    void infoGoesToStdout() {
        logger.info("started");

        assertThat(out.toString(StandardCharsets.UTF_8)).contains("[INFO] started");
        assertThat(err.toString(StandardCharsets.UTF_8)).isEmpty();
    }

    @Test
    void incomingAndOutgoingGoToStdout() {
        logger.logIncoming(user, "/help");
        logger.logOutgoing(user, "reply");

        assertThat(out.toString(StandardCharsets.UTF_8))
                .contains("[BOT IN] <- u1: /help")
                .contains("[BOT OUT] -> u1: reply");
    }

    @Test
    void errorGoesToStderrWithStackTrace() {
        logger.error("boom", new IllegalStateException("bad"));

        assertThat(err.toString(StandardCharsets.UTF_8))
                .contains("[ERROR] boom")
                .contains("IllegalStateException: bad");
        assertThat(out.toString(StandardCharsets.UTF_8)).isEmpty();
    }
}