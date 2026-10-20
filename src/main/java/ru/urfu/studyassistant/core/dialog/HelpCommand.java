package ru.urfu.studyassistant.core.dialog;

import ru.urfu.studyassistant.core.model.UserId;

public class HelpCommand implements Command {

    private static final String HELP_TEXT = """
            Привет! Я бот-расписание. Умею:
              /today       — пары на сегодня
              /week        — пары на текущую неделю
              /add <дата YYYY-MM-DD>, <время HH:mm>, <предмет>, <преподаватель>, <аудитория>
                           — добавить пару
              /help        — показать это сообщение
              /exit        — попрощаться
            """;

    @Override
    public boolean matches(String text) {
        return text.trim().equals("/help");
    }

    @Override
    public String execute(UserId userId, String text) {
        return HELP_TEXT;
    }
}