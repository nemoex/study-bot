package ru.urfu.studyassistant.core.dialog;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.port.Message;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BotEngineTest {

    private final UserId user = new UserId("test-user");

    private Command fakeCommand(String trigger, String response) {
        return new Command() {
            @Override public boolean matches(String text) { return text.trim().equals(trigger); }
            @Override public String execute(UserId userId, String text) { return response; }
        };
    }

    @Test
    void routesToMatchingCommand() {
        BotEngine engine = new BotEngine(List.of(fakeCommand("/ping", "pong")));

        String reply = engine.onMessage(new Message(user, "/ping"));

        assertThat(reply).isEqualTo("pong");
    }

    @Test
    void routesToFirstMatchingCommandOnly() {
        BotEngine engine = new BotEngine(List.of(
                fakeCommand("/ping", "first"),
                fakeCommand("/ping", "second")
        ));

        String reply = engine.onMessage(new Message(user, "/ping"));

        assertThat(reply).isEqualTo("first");
    }

    @Test
    void returnsFallbackWhenNothingMatches() {
        BotEngine engine = new BotEngine(List.of(fakeCommand("/ping", "pong")));

        String reply = engine.onMessage(new Message(user, "привет"));

        assertThat(reply).contains("/help");
    }

    @Test
    void worksWithRealHelpCommand() {
        BotEngine engine = new BotEngine(List.of(new HelpCommand()));

        String reply = engine.onMessage(new Message(user, "/help"));

        assertThat(reply).contains("/today", "/week", "/add", "/exit");
    }

    @Test
    void passesUserIdToCommand() {
        Command spy = new Command() {
            @Override public boolean matches(String text) { return true; }
            @Override public String execute(UserId userId, String text) {
                return "user=" + userId.value();
            }
        };
        BotEngine engine = new BotEngine(List.of(spy));

        String reply = engine.onMessage(new Message(user, "anything"));

        assertThat(reply).isEqualTo("user=test-user");
    }
}
