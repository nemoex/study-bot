package ru.urfu.studyassistant.app;

import ru.urfu.studyassistant.core.dialog.BotEngine;
import ru.urfu.studyassistant.core.port.ChatPlatform;
import ru.urfu.studyassistant.core.schedule.InMemoryScheduleProvider;
import ru.urfu.studyassistant.core.schedule.ScheduleProvider;
import ru.urfu.studyassistant.core.schedule.ScheduleService;
import ru.urfu.studyassistant.infrastructure.console.ConsoleChatPlatform;

import java.util.Scanner;

public class MainConsole {

    public static void main(String[] args) {
        ScheduleProvider provider = InMemoryScheduleProvider.withDemoData();
        ScheduleService service = new ScheduleService(provider);
        BotEngine engine = new BotEngine(service);

        ChatPlatform platform = new ConsoleChatPlatform(new Scanner(System.in));

        platform.setHandler(text -> platform.send(engine.handle(text)));
        platform.send("Привет! Я бот-расписание. Введите /help для списка команд.");
        platform.run();
    }
}