package ru.urfu.studyassistant.app;

import ru.urfu.studyassistant.core.dialog.AddLessonCommand;
import ru.urfu.studyassistant.core.dialog.BotEngine;
import ru.urfu.studyassistant.core.dialog.Command;
import ru.urfu.studyassistant.core.dialog.HelpCommand;
import ru.urfu.studyassistant.core.dialog.LessonParser;
import ru.urfu.studyassistant.core.dialog.TodayCommand;
import ru.urfu.studyassistant.core.dialog.WeekCommand;
import ru.urfu.studyassistant.core.port.MessageHandler;
import ru.urfu.studyassistant.core.schedule.ScheduleRepository;
import ru.urfu.studyassistant.core.schedule.ScheduleService;
import ru.urfu.studyassistant.infrastructure.persistence.InMemoryScheduleRepository;
import ru.urfu.studyassistant.infrastructure.vk.VkChatPlatform;
import ru.urfu.studyassistant.infrastructure.vk.VkConfig;

import java.time.Clock;
import java.util.List;

public class MainVk {

    public static void main(String[] args) {
        String groupId = System.getenv("VK_GROUP_ID");
        String accessToken = System.getenv("VK_BOT_TOKEN");

        if (groupId == null || groupId.isBlank()) {
            System.err.println("Ошибка: задайте переменную окружения VK_GROUP_ID");
            System.exit(1);
        }
        if (accessToken == null || accessToken.isBlank()) {
            System.err.println("Ошибка: задайте переменную окружения VK_BOT_TOKEN");
            System.exit(1);
        }
        VkConfig config = VkConfig.fromValues(groupId, accessToken);


        ScheduleRepository repository = new InMemoryScheduleRepository();
        Clock clock = Clock.systemDefaultZone();
        ScheduleService scheduleService = new ScheduleService(repository, clock);
        LessonParser lessonParser = new LessonParser();


        List<Command> commands = List.of(
                new HelpCommand(),
                new TodayCommand(scheduleService, clock),
                new WeekCommand(scheduleService, clock),
                new AddLessonCommand(scheduleService, lessonParser)
        );


        BotEngine engine = new BotEngine(commands);
        MessageHandler handler = new LoggingMessageHandler(engine);


        VkChatPlatform platform = new VkChatPlatform(config.groupId(), config.accessToken());
        platform.setHandler(handler);

        System.out.println("VK-бот запущен для группы " + config.groupId());
        platform.run();
    }
}