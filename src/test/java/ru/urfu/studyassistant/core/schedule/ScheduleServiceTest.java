package ru.urfu.studyassistant.core.schedule;

import ru.urfu.studyassistant.core.model.Lesson;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleServiceTest {
    
    @Test
    void emptyDay() {
        var service = new ScheduleService(new InMemoryScheduleProvider(Map.of()));
        String text = service.formatDay(DayOfWeek.MONDAY);
        assertTrue(text.contains("пар нет"));
    }

    @Test
    void dayWithLesson() {
        Lesson l = new Lesson("Матанализ", "Белоусова В.И.", "Р-401",
                LocalTime.of(14, 15), LocalTime.of(15, 45));
        var service = new ScheduleService(
                new InMemoryScheduleProvider(Map.of(DayOfWeek.MONDAY, List.of(l))));

        String text = service.formatDay(DayOfWeek.MONDAY);
        assertTrue(text.contains("Матанализ"));
        assertTrue(text.contains("Р-401"));
    }

    @Test
    void week() {
        var service = new ScheduleService(new InMemoryScheduleProvider(Map.of()));
        String text = service.formatWeek();
        for (DayOfWeek day : DayOfWeek.values()) {
            assertTrue(text.contains(day.toString()), "нет дня " + day);
        }
    }
}
