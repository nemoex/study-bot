package ru.urfu.studyassistant.core.dialog;

import ru.urfu.studyassistant.core.schedule.ScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class BotEngine {

    private final ScheduleService scheduleService;

    public BotEngine(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    public String handle(String input) {
        if (input == null || input.isBlank()) {
            return "Введите команду. /help — список команд.";
        }
        String trimmed = input.trim();
        return switch (trimmed) {
            case "/help" -> helpText();
            case "/today" -> scheduleService.formatDay(LocalDate.now().getDayOfWeek());
            case "/week" -> scheduleService.formatWeek();
            case "/exit" -> "До встречи!";
            default -> handleDayOrUnknown(trimmed);
        };
    }

    private String handleDayOrUnknown(String input) {
        DayOfWeek day = parseDay(input);
        if (day != null) {
            return scheduleService.formatDay(day);
        }
        return "Не знаю такой команды. Введите /help для списка команд.";
    }

    private DayOfWeek parseDay(String input) {
        return switch (input.toLowerCase()) {
            case "понедельник", "пн" -> DayOfWeek.MONDAY;
            case "вторник", "вт" -> DayOfWeek.TUESDAY;
            case "среда", "ср" -> DayOfWeek.WEDNESDAY;
            case "четверг", "чт" -> DayOfWeek.THURSDAY;
            case "пятница", "пт" -> DayOfWeek.FRIDAY;
            case "суббота", "сб" -> DayOfWeek.SATURDAY;
            case "воскресенье", "вс" -> DayOfWeek.SUNDAY;
            default -> null;
        };
    }

    private String helpText() {
        return """
            Я бот-расписание. Умею:
              /today — пары на сегодня
              /week  — пары на неделю
              <день недели> — пары на конкретный день
              /help  — эта справка
              /exit  — выйти
            """;
    }
}