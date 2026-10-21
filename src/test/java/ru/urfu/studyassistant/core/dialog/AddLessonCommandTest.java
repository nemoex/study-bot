package ru.urfu.studyassistant.core.dialog;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.schedule.ScheduleRepository;
import ru.urfu.studyassistant.core.schedule.ScheduleService;
import ru.urfu.studyassistant.infrastructure.persistence.InMemoryScheduleRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class AddLessonCommandTest {

    @Test
    void matchesCorrectly() {
        AddLessonCommand command = new AddLessonCommand(null, null);
        assertThat(command.matches("/add")).isTrue();
        assertThat(command.matches("/add 2026-10-11, 08:30, Матанализ, Белоусова В.И., Р-215")).isTrue();
        assertThat(command.matches("/today")).isFalse();
    }

    @Test
    void returnsHelpOnEmptyArgs() {
        AddLessonCommand command = new AddLessonCommand(null, null);
        UserId user = new UserId("user1");
        
        String response = command.execute(user, "/add");
        
        assertThat(response).contains("Использование: /add");
    }

    @Test
    void executesAndAddsLesson() {
        Clock clock = Clock.fixed(Instant.parse("2026-10-01T10:00:00Z"), ZoneId.of("UTC"));
        ScheduleRepository repo = new InMemoryScheduleRepository();
        ScheduleService service = new ScheduleService(repo, clock);
        LessonParser parser = new LessonParser();
        
        AddLessonCommand command = new AddLessonCommand(service, parser);
        UserId user = new UserId("user1");
        
        String response = command.execute(user, "/add 2026-10-11, 08:30, Матанализ, Белоусова В.И., Р-215");
        
        assertThat(response).contains("Добавлено:");
        assertThat(response).contains("2026-10-11");
        assertThat(response).contains("08:30-10:00");
        assertThat(response).contains("Матанализ");
    }
}