package ru.urfu.studyassistant.core.dialog;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LessonParserTest {

    @Test
    void parsesValidLessonString() {
        LessonParser parser = new LessonParser();
        String input = "2026-10-11, 08:30, Матанализ, Белоусова В.И., Р-215";

        Lesson lesson = parser.parse(input);

        assertThat(lesson).isNotNull();
        assertThat(lesson.date()).isEqualTo(LocalDate.of(2026, 10, 11));
        assertThat(lesson.startTime()).isEqualTo(LocalTime.of(8, 30));
        assertThat(lesson.subject()).isEqualTo("Матанализ");
        assertThat(lesson.teacher()).isEqualTo("Белоусова В.И.");
        assertThat(lesson.room()).isEqualTo("Р-215");
    }

    @Test
    void throwsExceptionOnInvalidFormat() {
        LessonParser parser = new LessonParser();
        assertThatThrownBy(() -> parser.parse("test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Неверный формат. Использование: /add <дата YYYY-MM-DD>, <время HH:mm>, <предмет>, <преподаватель>, <аудитория>");
    }
}