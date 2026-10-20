package ru.urfu.studyassistant.infrastructure.console;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.port.Message;
import ru.urfu.studyassistant.core.port.MessageHandler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleChatPlatformTest {

    private final UserId user = new UserId("alice");

    // Хендлер, который запоминает, что ему пришло, и отвечает "reply:<текст>".
    private static class CapturingHandler implements MessageHandler {
        final List<Message> received = new ArrayList<>();
        @Override
        public String onMessage(Message message) {
            received.add(message);
            return "reply:" + message.text();
        }
    }

    private record TestPlatform(ConsoleChatPlatform platform, ByteArrayOutputStream buffer) {
        String output() { return buffer.toString(StandardCharsets.UTF_8); }
    }

    private TestPlatform make(String input) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(input);
        ConsoleChatPlatform platform = new ConsoleChatPlatform(scanner, user, out);
        return new TestPlatform(platform, buffer);
    }

    @Test
    void sendWritesTextToOutputStream() {
        TestPlatform p = make("");
        p.platform().send("hello");
        assertThat(p.output()).contains("hello");
    }

    @Test
    void runProcessesEachLineThroughHandler() {
        TestPlatform p = make("hello\nworld\n");
        CapturingHandler handler = new CapturingHandler();
        p.platform().setHandler(handler);

        p.platform().run();

        assertThat(handler.received).hasSize(2);
        assertThat(handler.received.get(0).text()).isEqualTo("hello");
        assertThat(handler.received.get(1).text()).isEqualTo("world");
        assertThat(p.output()).contains("reply:hello", "reply:world");
    }

    @Test
    void runPassesUserIdToHandler() {
        TestPlatform p = make("hello\n");
        CapturingHandler handler = new CapturingHandler();
        p.platform().setHandler(handler);

        p.platform().run();

        assertThat(handler.received).hasSize(1);
        assertThat(handler.received.get(0).userId()).isEqualTo(user);
    }

    @Test
    void runStopsOnExitAndPrintsFarewell() {
        TestPlatform p = make("hello\n/exit\nworld\n");
        CapturingHandler handler = new CapturingHandler();
        p.platform().setHandler(handler);

        p.platform().run();

        assertThat(handler.received).hasSize(1);
        assertThat(handler.received.get(0).text()).isEqualTo("hello");
        assertThat(p.output()).contains("До встречи!", "reply:hello");
        assertThat(p.output()).doesNotContain("reply:world");
    }

    @Test
    void runReturnsImmediatelyOnEmptyInput() {
        TestPlatform p = make("");
        CapturingHandler handler = new CapturingHandler();
        p.platform().setHandler(handler);

        p.platform().run();

        assertThat(handler.received).isEmpty();
    }
}