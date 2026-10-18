package ru.urfu.studyassistant.core.schedule;

import ru.urfu.studyassistant.core.model.Lesson;
import ru.urfu.studyassistant.core.model.UserId;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScheduleService {
    private final ScheduleRepository repo;
    private final Clock clock;

    public ScheduleService(ScheduleRepository repo, Clock clock) {
        this.repo = repo;
        this.clock = clock;
    }

    public void addLesson(UserId userId, Lesson lesson) {
        // проверка: нельзя добавить ранее
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime lessonStart = lesson.date().atTime(lesson.startTime());
        
        if (lessonStart.isBefore(now)) {
            throw new IllegalArgumentException();
        }
        // проверка: конфликт расписания (пересечение)
        List<Lesson> dayLessons = repo.findByDate(userId, lesson.date());
        for (Lesson existing : dayLessons) {
            boolean overlaps = lesson.startTime().isBefore(existing.endTime()) && 
                               existing.startTime().isBefore(lesson.endTime());
            
            if (overlaps) {
                throw new IllegalArgumentException(String.format(
                    "Конфликт: на это время уже стоит «%s» (%s-%s).",
                    existing.subject(), existing.startTime(), existing.endTime()
                ));
            }
        }
        repo.add(userId, lesson);
    }

    public String formatDay(UserId userId, LocalDate date) {
        List<Lesson> lessons = repo.findByDate(userId, date);

        if (lessons.isEmpty()) {
            return String.format("На %s пар нет.", date);
        }

        List<Lesson> sorted = new ArrayList<>(lessons);
        sorted.sort(Comparator.comparing(Lesson::startTime));

        StringBuilder sb = new StringBuilder();
        sb.append(date).append(":\n");

        for (int i = 0; i < sorted.size(); i++) {
            Lesson l = sorted.get(i);
            sb.append(String.format("%d. %s–%s  %s  (%s, ауд. %s)\n",
                    i + 1, l.startTime(), l.endTime(), l.subject(), l.teacher(), l.room()));
        }
        return sb.toString().trim();
    }

    public String formatWeek(UserId userId, LocalDate anchorDate) {
        LocalDate startOfWeek = anchorDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = anchorDate.with(DayOfWeek.SUNDAY);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Расписание на неделю %s - %s:\n", startOfWeek, endOfWeek));

        boolean hasLessons = false;
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);
            List<Lesson> daily = repo.findByDate(userId, currentDate);
            
            if (!daily.isEmpty()) {
                hasLessons = true;
                sb.append("   ").append(currentDate).append(":\n");
                
                List<Lesson> sorted = new ArrayList<>(daily);
                sorted.sort(Comparator.comparing(Lesson::startTime));
                
                for (int j = 0; j < sorted.size(); j++) {
                    Lesson l = sorted.get(j);
                    sb.append(String.format("%d. %s-%s  %s  (%s, ауд. %s)\n",
                            j + 1, l.startTime(), l.endTime(), l.subject(), l.teacher(), l.room()));
                }
            }
        }

        if (!hasLessons) {
            return String.format("На неделю %s - %s пар нет.", startOfWeek, endOfWeek);
        }

        return sb.toString().trim();
    }
}