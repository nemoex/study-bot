package ru.urfu.studyassistant.core.dialog;

import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.schedule.ScheduleService;

import java.time.Clock;
import java.time.LocalDate;

public class WeekCommand implements Command {
    private final ScheduleService service;
    private final Clock clock;

    public WeekCommand(ScheduleService service, Clock clock) {
        this.service = service;
        this.clock = clock;
    }

    @Override
    public boolean matches(String text) {
        return text.trim().equals("/week");
    }

    @Override
    public String execute(UserId userId, String text) {
        LocalDate today = LocalDate.now(clock);
        return service.formatWeek(userId, today);
    }
}