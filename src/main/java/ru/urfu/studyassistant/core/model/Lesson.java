package ru.urfu.studyassistant.core.model;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;
import java.util.Objects;

public record Lesson(
    LocalDate date,
    LocalTime startTime,
    String subject,
    String teacher,
    String room
) {
    public static final Duration DUR = Duration.ofMinutes(90);
    public Lesson {
        Objects.requireNonNull(date);
        Objects.requireNonNull(startTime);
        
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (teacher == null || teacher.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (room == null || room.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public LocalTime endTime() {
        return startTime.plus(DUR);
    }
}