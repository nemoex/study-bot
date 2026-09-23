package ru.urfu.studyassistant.core.schedule;

import ru.urfu.studyassistant.core.model.Lesson;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryScheduleProviderTest {

    @Test
    void emptyDay() {
        var provider = new InMemoryScheduleProvider(Map.of());
        assertTrue(provider.getLessons(DayOfWeek.MONDAY).isEmpty());
    }

    @Test
    void dayWithLessons() {
        Lesson l = new Lesson("Алгоритмы", "Кирилл Решке", "Р-121", 
            LocalTime.of(8, 30), LocalTime.of(10, 0));
        var provider = new InMemoryScheduleProvider(Map.of(DayOfWeek.THURSDAY, List.of(l)));
        assertEquals(List.of(l), provider.getLessons(DayOfWeek.THURSDAY));
    }

    @Test 
    void demoMonday() {
        var provider = InMemoryScheduleProvider.withDemoData();
        assertFalse(provider.getLessons(DayOfWeek.MONDAY).isEmpty());
    }

    @Test
    void demoSunday() {
        var provider = InMemoryScheduleProvider.withDemoData();
        assertTrue(provider.getLessons(DayOfWeek.SUNDAY).isEmpty());
    }    
}
