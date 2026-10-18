package ru.urfu.studyassistant.infrastructure.persistence;

import ru.urfu.studyassistant.core.model.Lesson;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.schedule.ScheduleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryScheduleRepository implements ScheduleRepository {
    private final Map<UserId, List<Lesson>> data = new HashMap<>();

    @Override
    public List<Lesson> findByDate(UserId userId, LocalDate date) {
        return data.getOrDefault(userId, List.of())
                .stream()
                .filter(lesson -> lesson.date().equals(date))
                .toList();
    }

    @Override
    public List<Lesson> findByDateRange(UserId userId, LocalDate from, LocalDate to) {
        return data.getOrDefault(userId, List.of())
                .stream()
                .filter(lesson -> !lesson.date().isBefore(from) && !lesson.date().isAfter(to))
                .toList();
    }

    @Override
    public void add(UserId userId, Lesson lesson) {
        data.computeIfAbsent(userId, k -> new ArrayList<>()).add(lesson);
    }
}