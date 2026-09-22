package ru.urfu.studyassistant.core.model;

import ru.urfu.studyassistant.core.model.Lesson;

import java.time.DayOfWeek;
import java.util.List;

/**
 * источник данных о расписании, никогда не возвращает null
 */

public interface ScheduleProvider {
    /**
     * занятия на указанный день недели
     */
    List<Lesson> getLessons(DayOfWeek day);
}