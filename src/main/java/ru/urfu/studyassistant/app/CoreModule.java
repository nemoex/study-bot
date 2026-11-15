package ru.urfu.studyassistant.app;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.urfu.studyassistant.core.dialog.AddLessonCommand;
import ru.urfu.studyassistant.core.dialog.BotEngine;
import ru.urfu.studyassistant.core.dialog.Command;
import ru.urfu.studyassistant.core.dialog.HelpCommand;
import ru.urfu.studyassistant.core.dialog.LessonParser;
import ru.urfu.studyassistant.core.dialog.TodayCommand;
import ru.urfu.studyassistant.core.dialog.WeekCommand;
import ru.urfu.studyassistant.core.schedule.ScheduleRepository;
import ru.urfu.studyassistant.core.schedule.ScheduleService;
import ru.urfu.studyassistant.infrastructure.logging.BotLogger;
import ru.urfu.studyassistant.infrastructure.logging.ConsoleBotLogger;

import java.time.Clock;
import java.util.List;

public class CoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BotLogger.class).to(ConsoleBotLogger.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Provides
    @Singleton
    LessonParser lessonParser() {
        return new LessonParser();
    }

    @Provides
    @Singleton
    ScheduleService scheduleService(ScheduleRepository repository, Clock clock) {
        return new ScheduleService(repository, clock);
    }

    @Provides
    @Singleton
    List<Command> commands(ScheduleService scheduleService, Clock clock, LessonParser lessonParser) {
        return List.of(
                new HelpCommand(),
                new TodayCommand(scheduleService, clock),
                new WeekCommand(scheduleService, clock),
                new AddLessonCommand(scheduleService, lessonParser)
        );
    }

    @Provides
    @Singleton
    BotEngine botEngine(List<Command> commands) {
        return new BotEngine(commands);
    }
}