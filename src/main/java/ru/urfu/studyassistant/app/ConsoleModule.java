package ru.urfu.studyassistant.app;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.urfu.studyassistant.core.dialog.BotEngine;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.schedule.ScheduleRepository;
import ru.urfu.studyassistant.infrastructure.console.ConsoleChatPlatform;
import ru.urfu.studyassistant.infrastructure.persistence.InMemoryScheduleRepository;

import java.util.Scanner;

public class ConsoleModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new CoreModule());
        bind(ScheduleRepository.class).to(InMemoryScheduleRepository.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    ConsoleChatPlatform consoleChatPlatform(BotEngine engine) {
        ConsoleChatPlatform platform = new ConsoleChatPlatform(new Scanner(System.in), new UserId("local-user"));
        platform.setHandler(engine);
        return platform;
    }
}