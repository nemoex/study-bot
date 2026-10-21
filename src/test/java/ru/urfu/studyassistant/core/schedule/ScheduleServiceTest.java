package ru.urfu.studyassistant.core.schedule;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.Lesson;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.infrastructure.persistence.InMemoryScheduleRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleServiceTest {
    Clock fixedClock = Clock.fixed(Instant.parse("2026-10-01T10:00:00Z"), ZoneId.of("UTC"));
    UserId testUser = new UserId("test-user");

    @Test
    void emptyDay() {
        var service = new ScheduleService(new InMemoryScheduleRepository(), fixedClock);
        var date = LocalDate.of(2026, 10, 11);
        
        String text = service.formatDay(testUser, date);
        assertTrue(text.contains("пар нет"));
    }

    @Test
    void dayWithLesson() {
        var repo = new InMemoryScheduleRepository();
        var service = new ScheduleService(repo, fixedClock);
        var date = LocalDate.of(2026, 10, 11);
        
        Lesson l = new Lesson(date, LocalTime.of(14, 15), "Матанализ", "Белоусова В.И.", "Р-401");
        service.addLesson(testUser, l);

        String text = service.formatDay(testUser, date);
        assertTrue(text.contains("Матанализ"));
        assertTrue(text.contains("Р-401"));
    }

    @Test
    void cannotAddLessonInPast() {
        var repo = new InMemoryScheduleRepository();
        var service = new ScheduleService(repo, fixedClock);
                var pastDate = LocalDate.of(2020, 1, 1);
        Lesson l = new Lesson(pastDate, LocalTime.of(9, 0), "ООП", "Денис Матафонов", "Р-101");

        assertThrows(IllegalArgumentException.class, () -> service.addLesson(testUser, l));
    }

    @Test
    void cannotAddOverlappingLessons() {
        var repo = new InMemoryScheduleRepository();
        var service = new ScheduleService(repo, fixedClock);
        var date = LocalDate.of(2026, 10, 11);

        Lesson l1 = new Lesson(date, LocalTime.of(9, 0), "ООП", "Денис Матафонов", "Р-101");
        service.addLesson(testUser, l1);

        Lesson l2 = new Lesson(date, LocalTime.of(9, 30), "Алгоритмы", "Кирилл Решке", "Р-202");
        assertThrows(IllegalArgumentException.class, () -> service.addLesson(testUser, l2));
    }
}