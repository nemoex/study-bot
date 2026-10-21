package ru.urfu.studyassistant.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.Lesson;
import ru.urfu.studyassistant.core.model.UserId;

import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryScheduleRepositoryTest {

    @Test
    void emptyDay() {
        var repo = new InMemoryScheduleRepository();
        var user = new UserId("test-user");
        var date = LocalDate.of(2026, 10, 11);
        
        assertTrue(repo.findByDate(user, date).isEmpty());
    }

    @Test
    void dayWithLessons() {
        var repo = new InMemoryScheduleRepository();
        var user = new UserId("test-user");
        var date = LocalDate.of(2026, 10, 11);
        
        Lesson l = new Lesson(date, LocalTime.of(8, 30), "Алгоритмы", "Кирилл Решке", "Р-121");
        repo.add(user, l);
        
        var lessons = repo.findByDate(user, date);
        assertEquals(1, lessons.size());
        assertEquals("Алгоритмы", lessons.get(0).subject());
    }

    @Test
    void shouldIsolateUsersSchedules() {
        var repo = new InMemoryScheduleRepository();
        var user1 = new UserId("u1");
        var user2 = new UserId("u2");
        var date = LocalDate.of(2026, 10, 11);

        Lesson l = new Lesson(date, LocalTime.of(8, 30), "Матанализ", "Белоусова В.И.", "Р-215");
        repo.add(user1, l);

        assertFalse(repo.findByDate(user1, date).isEmpty());
        assertTrue(repo.findByDate(user2, date).isEmpty());
    }
}