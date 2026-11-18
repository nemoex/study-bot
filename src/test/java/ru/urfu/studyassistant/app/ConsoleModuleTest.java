package ru.urfu.studyassistant.app;

import com.google.inject.Guice;
import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.dialog.BotEngine;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.port.Message;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleModuleTest {

    @Test
    void wiresWorkingEngineWithoutDatabase() {
        BotEngine engine = Guice.createInjector(new ConsoleModule()).getInstance(BotEngine.class);

        String reply = engine.onMessage(new Message(new UserId("u1"), "/help"));

        assertThat(reply).contains("/today", "/week", "/add");
    }
}