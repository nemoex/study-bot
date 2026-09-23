package ru.urfu.studyassistant.core.schedule;

import ru.urfu.studyassistant.core.model.Lesson;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Map;
import java.util.EnumMap;
import java.util.List;

/**
 * хранит расписание в памяти
 */

public class InMemoryScheduleProvider implements ScheduleProvider {
    private final Map<DayOfWeek, List<Lesson>> lessons;
    public InMemoryScheduleProvider(Map<DayOfWeek, List<Lesson>> lessons) {
        this.lessons = new EnumMap<>(DayOfWeek.class);
        this.lessons.putAll(lessons);
    }

    // занятия на день
    @Override
    public List<Lesson> getLessons(DayOfWeek day) {
        return lessons.getOrDefault(day, List.of());
    }

    // демо расписания для тестов и запуска
    public static InMemoryScheduleProvider withDemoData() {
        Map<DayOfWeek, List<Lesson>> data = new EnumMap<>(DayOfWeek.class);
        
        data.put(DayOfWeek.MONDAY, List.of(
            new Lesson("Матанализ", "Белоусова В.И", "Р-215", LocalTime.of(8, 30), LocalTime.of(10, 0)),
            new Lesson("ООП", "Денис Матафонов", "Р-101", LocalTime.of(10, 15), LocalTime.of(11, 45))
        ));

        data.put(DayOfWeek.WEDNESDAY, List.of(
            new Lesson("Дискретная математика", "Расин О.В", "Т-333", LocalTime.of(14, 15), LocalTime.of(15, 45))
        ));

        data.put(DayOfWeek.FRIDAY, List.of(
            new Lesson("Алгоритмы", "Кирилл Решке", "Р-052", LocalTime.of(8, 30), LocalTime.of(10, 0))
        ));

        return new InMemoryScheduleProvider(data);
    }
}