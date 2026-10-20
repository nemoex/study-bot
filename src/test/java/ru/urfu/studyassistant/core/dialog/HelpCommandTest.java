package ru.urfu.studyassistant.core.dialog;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.UserId;

import static org.assertj.core.api.Assertions.assertThat;

class HelpCommandTest {

    private final HelpCommand command = new HelpCommand();
    private final UserId user = new UserId("test-user");

    @Test
    void matchesExactHelp() {
        assertThat(command.matches("/help")).isTrue();
    }

    @Test
    void matchesHelpWithTrailingSpaces() {
        assertThat(command.matches("  /help  ")).isTrue();
    }

    @Test
    void doesNotMatchOtherCommands() {
        assertThat(command.matches("/today")).isFalse();
        assertThat(command.matches("/week")).isFalse();
        assertThat(command.matches("/add")).isFalse();
        assertThat(command.matches("привет")).isFalse();
    }

    @Test
    void executeReturnsHelpText() {
        String text = command.execute(user, "/help");

        assertThat(text).isNotBlank();
        assertThat(text).contains("/today", "/week", "/add", "/help", "/exit");
    }
}