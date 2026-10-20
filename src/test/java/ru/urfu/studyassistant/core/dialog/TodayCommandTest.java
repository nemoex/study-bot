package ru.urfu.studyassistant.core.dialog;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.schedule.ScheduleService;
import ru.urfu.studyassistant.infrastructure.persistence.InMemoryScheduleRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class TodayCommandTest {

    @Test
    void matchesAndReturnsTodaySchedule() {
        Clock clock = Clock.fixed(Instant.parse("2026-10-11T10:00:00Z"), ZoneId.of("UTC"));
        ScheduleService service = new ScheduleService(new InMemoryScheduleRepository(), clock);
        TodayCommand command = new TodayCommand(service, clock);

        assertThat(command.matches("/today")).isTrue();
        assertThat(command.matches("/week")).isFalse();

        String response = command.execute(new UserId("user1"), "/today");
        assertThat(response).isEqualTo("На 2026-10-11 пар нет.");
    }
}