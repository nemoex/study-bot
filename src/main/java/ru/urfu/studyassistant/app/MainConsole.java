package ru.urfu.studyassistant.app;

import com.google.inject.Guice;
import ru.urfu.studyassistant.infrastructure.console.ConsoleChatPlatform;

public final class MainConsole {

    public static void main(String[] args) {
        Guice.createInjector(new ConsoleModule())
                .getInstance(ConsoleChatPlatform.class)
                .run();
    }
}