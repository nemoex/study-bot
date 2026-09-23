package ru.urfu.studyassistant.core.schedule;

import ru.urfu.studyassistant.core.model.Lesson;

import java.time.DayOfWeek;
import java.util.List;

/**
 * превращает список занятий в текст
 */
public class ScheduleService {
    private final ScheduleProvider provider;
    public ScheduleService(ScheduleProvider provider) {
        this.provider = provider;
    }

    // текст на день
    public String formatDay(DayOfWeek day) {
        List<Lesson> lessons = provider.getLessons(day);

        if (lessons.isEmpty()) {
            return day + ": пар нет\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(day).append(":\n");

        for (int i = 0; i < lessons.size(); i++) {
            Lesson l = lessons.get(i);
            sb.append(String.format("  %d. %s, %s-%s, ауд. %s, %s%n", 
                i+1, l.subject(), l.start(), l.end(), l.room(), l.teacher()));
        }
        return sb.toString();
    }

    // текст на неделю
    public String formatWeek() {
        StringBuilder sb = new StringBuilder();
        for (DayOfWeek day : DayOfWeek.values()) {
            sb.append(formatDay(day));
        }
        return sb.toString();
    }
}
