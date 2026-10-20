package ru.urfu.studyassistant.app;

import ru.urfu.studyassistant.core.dialog.BotEngine;
import ru.urfu.studyassistant.core.dialog.HelpCommand;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.infrastructure.console.ConsoleChatPlatform;

import java.util.List;
import java.util.Scanner;

public class MainConsole {
    public static void main(String[] args) {
        ConsoleChatPlatform platform = new ConsoleChatPlatform(
                new Scanner(System.in),
                new UserId("local-user")
        );
        BotEngine engine = new BotEngine(List.of(new HelpCommand()));
        platform.setHandler(engine);
        platform.run();
    }
}
