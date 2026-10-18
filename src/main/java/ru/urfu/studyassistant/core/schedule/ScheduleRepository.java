package ru.urfu.studyassistant.core.schedule;

import ru.urfu.studyassistant.core.model.Lesson;
import ru.urfu.studyassistant.core.model.UserId;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {
    List<Lesson> findByDate(UserId userId, LocalDate date);
    List<Lesson> findByDateRange(UserId userId, LocalDate from, LocalDate to);
    void add(UserId userId, Lesson lesson);
}