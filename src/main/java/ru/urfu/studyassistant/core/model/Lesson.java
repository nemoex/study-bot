package ru.urfu.studyassistant.core.model;

import java.time.LocalTime;

public record Lesson(
    String subject,
    String teacher,
    String room,
    LocalTime start,
    LocalTime end
) {}