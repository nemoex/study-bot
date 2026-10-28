package ru.urfu.studyassistant.app;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.port.Message;
import ru.urfu.studyassistant.core.port.MessageHandler;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoggingMessageHandlerTest {

    @Test
    void delegatesAndReturnsWrappedResult() {
        MessageHandler inner = msg -> "reply:" + msg.text();
        LoggingMessageHandler logging = new LoggingMessageHandler(inner);

        String result = logging.onMessage(new Message(new UserId("u1"), "hi"));

        assertThat(result).isEqualTo("reply:hi");
    }

    @Test
    void passesMessageThroughUnchanged() {
        List<Message> seen = new ArrayList<>();
        MessageHandler inner = msg -> { seen.add(msg); return "ok"; };
        LoggingMessageHandler logging = new LoggingMessageHandler(inner);

        Message input = new Message(new UserId("alice"), "/help");
        logging.onMessage(input);

        assertThat(seen).hasSize(1);
        assertThat(seen.get(0)).isEqualTo(input);
    }
}