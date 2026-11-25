package ru.urfu.studyassistant.core.schedule;

import ru.urfu.studyassistant.core.model.Lesson;
import ru.urfu.studyassistant.core.model.UserId;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ScheduleService {
    private final ScheduleRepository repo;
    private final Clock clock;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM");
    private static final DateTimeFormatter DAY_OF_WEEK_FORMAT = DateTimeFormatter.ofPattern("EEEE", Locale.of("ru"));

    public ScheduleService(ScheduleRepository repo, Clock clock) {
        this.repo = repo;
        this.clock = clock;
    }

    public void addLesson(UserId userId, Lesson lesson) {
        // проверка: нельзя добавить ранее
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime lessonStart = lesson.date().atTime(lesson.startTime());

        if (lessonStart.isBefore(now)) {
            throw new IllegalArgumentException("Ошибка: нельзя добавлять в прошлое.");
        }
        // проверка: конфликт расписания (пересечение)
        List<Lesson> dayLessons = repo.findByDate(userId, lesson.date());
        for (Lesson existing : dayLessons) {
            boolean overlaps = lesson.startTime().isBefore(existing.endTime()) &&
                    existing.startTime().isBefore(lesson.endTime());

            if (overlaps) {
                throw new IllegalArgumentException(String.format(
                        "⚠️ Конфликт: на это время уже стоит «%s» (%s-%s).",
                        existing.subject(), existing.startTime(), existing.endTime()));
            }
        }
        repo.add(userId, lesson);
    }

    public String formatDay(UserId userId, LocalDate date) {
        List<Lesson> lessons = repo.findByDate(userId, date);

        if (lessons.isEmpty()) {
            return String.format("На %s пар нет.", date.format(DATE_FORMAT));
        }

        List<Lesson> sorted = new ArrayList<>(lessons);
        sorted.sort(Comparator.comparing(Lesson::startTime));

        StringBuilder sb = new StringBuilder();
        String dayName = getCapitalizedDayOfWeek(date);
        sb.append(String.format("📅 Твое расписание на %s (%s):\n\n", date.format(DATE_FORMAT), dayName));

        for (int i = 0; i < sorted.size(); i++) {
            Lesson l = sorted.get(i);
            sb.append(String.format("%d. 🕒 %s-%s | %s | %s, ауд. %s\n\n",
                    i + 1, l.startTime(), l.endTime(), l.subject(), l.teacher(), l.room()));
        }
        return sb.toString().trim();
    }

    public String formatWeek(UserId userId, LocalDate anchorDate) {
        LocalDate startOfWeek = anchorDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = anchorDate.with(DayOfWeek.SUNDAY);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("📆 Расписание на неделю (%s - %s):\n\n",
                startOfWeek.format(DATE_FORMAT), endOfWeek.format(DATE_FORMAT)));

        boolean hasLessons = false;
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);
            List<Lesson> daily = repo.findByDate(userId, currentDate);

            if (!daily.isEmpty()) {
                hasLessons = true;
                String dayName = getCapitalizedDayOfWeek(currentDate);

                sb.append(String.format("%s (%s):\n", currentDate.format(DATE_FORMAT), dayName));

                List<Lesson> sorted = new ArrayList<>(daily);
                sorted.sort(Comparator.comparing(Lesson::startTime));

                for (int j = 0; j < sorted.size(); j++) {
                    Lesson l = sorted.get(j);
                    sb.append(String.format("%d. 🕒 %s-%s | %s | %s, ауд. %s\n\n",
                            j + 1, l.startTime(), l.endTime(), l.subject(), l.teacher(), l.room()));
                }
                sb.append("\n");
            }
        }

        if (!hasLessons) {
            return String.format("На неделю %s - %s пар нет.", startOfWeek, endOfWeek);
        }

        return sb.toString().trim();
    }

    private String getCapitalizedDayOfWeek(LocalDate date) {
        String dayName = date.format(DAY_OF_WEEK_FORMAT);
        return Character.toUpperCase(dayName.charAt(0)) + dayName.substring(1);
    }
}