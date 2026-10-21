package ru.urfu.studyassistant.core.dialog;

import ru.urfu.studyassistant.core.model.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LessonParser {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Lesson parse(String input) {
        String[] parts = input.split(",");
        
        if (parts.length != 5) {
            throw new IllegalArgumentException("Неверный формат. Использование: /add <дата YYYY-MM-DD>, <время HH:mm>, <предмет>, <преподаватель>, <аудитория>");
        }

        try {
            LocalDate date = LocalDate.parse(parts[0].trim(), DATE_FORMATTER);
            LocalTime time = LocalTime.parse(parts[1].trim(), TIME_FORMATTER);
            String subject = parts[2].trim();
            String teacher = parts[3].trim();
            String room = parts[4].trim();

            return new Lesson(date, time, subject, teacher, room);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты или времени. Используйте YYYY-MM-DD и HH:mm.");
        }
    }
}