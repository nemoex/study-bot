package ru.urfu.studyassistant.core.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class LessonTest {

    @Test
    void calculatesEndTimeCorrectly() {
        Lesson lesson = new Lesson(
                LocalDate.of(2026, 10, 11),
                LocalTime.of(8, 30),
                "Матанализ",
                "Белоусова В.И.",
                "Р-215"
        );
        assertEquals(LocalTime.of(10, 0), lesson.endTime());
    }

    @Test
    void throwsExceptionOnEmptySubject() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Lesson(LocalDate.now(), LocalTime.now(), "   ", "Белоусова В.И.", "Р-215")
        );
    }
}