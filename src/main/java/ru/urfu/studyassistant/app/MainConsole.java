package ru.urfu.studyassistant.app;

import ru.urfu.studyassistant.core.dialog.*;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.schedule.ScheduleService;
import ru.urfu.studyassistant.infrastructure.console.ConsoleChatPlatform;
import ru.urfu.studyassistant.infrastructure.persistence.InMemoryScheduleRepository;

import java.time.Clock;
import java.util.List;
import java.util.Scanner;

public class MainConsole {
    public static void main(String[] args) {
        InMemoryScheduleRepository repository = new InMemoryScheduleRepository();
        Clock clock = Clock.systemDefaultZone();
        ScheduleService service = new ScheduleService(repository, clock);
        LessonParser parser = new LessonParser();

        ConsoleChatPlatform platform = new ConsoleChatPlatform(
                new Scanner(System.in),
                new UserId("local-user")
        );

        BotEngine engine = new BotEngine(List.of(
                new HelpCommand(),
                new AddLessonCommand(service, parser),
                new TodayCommand(service, clock),
                new WeekCommand(service, clock)
        ));

        platform.setHandler(engine);
        System.out.println("Бот запущен. Введите /add для добавления пары или /help.");
        platform.run();
    }
}
