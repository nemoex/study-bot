package ru.urfu.studyassistant.core.dialog;

import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.schedule.ScheduleService;

import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotEngineTest {

    private final ScheduleService scheduleService = mock(ScheduleService.class);
    private final BotEngine engine = new BotEngine(scheduleService);

    @Test
    void help_returnsHelpText() {
        String response = engine.handle("/help");
        assertThat(response)
                .contains("/today")
                .contains("/week")
                .contains("/help");
    }

    @Test
    void today_asksServiceForToday() {
        when(scheduleService.formatDay(any(DayOfWeek.class))).thenReturn("Пары на сегодня");
        String response = engine.handle("/today");
        assertThat(response).isEqualTo("Пары на сегодня");
        verify(scheduleService).formatDay(any(DayOfWeek.class));
    }

    @Test
    void week_asksServiceForWeek() {
        when(scheduleService.formatWeek()).thenReturn("Пары на неделю");
        String response = engine.handle("/week");
        assertThat(response).isEqualTo("Пары на неделю");
    }

    @Test
    void dayName_asksServiceForThatDay() {
        when(scheduleService.formatDay(DayOfWeek.MONDAY)).thenReturn("Понедельник");
        String response = engine.handle("понедельник");
        assertThat(response).isEqualTo("Понедельник");
        verify(scheduleService).formatDay(DayOfWeek.MONDAY);
    }

    @Test
    void unknownCommand_returnsErrorMessage() {
        String response = engine.handle("/foo");
        assertThat(response).contains("Не знаю");
    }

    @Test
    void emptyInput_returnsErrorMessage() {
        String response = engine.handle("");
        assertThat(response).isNotEmpty();
    }
}