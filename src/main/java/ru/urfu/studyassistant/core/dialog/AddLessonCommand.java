package ru.urfu.studyassistant.core.dialog;

import ru.urfu.studyassistant.core.model.Lesson;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.schedule.ScheduleService;

public class AddLessonCommand implements Command {

    private final ScheduleService service;
    private final LessonParser parser;

    public AddLessonCommand(ScheduleService service, LessonParser parser) {
        this.service = service;
        this.parser = parser;
    }

    @Override
    public boolean matches(String text) {
        return text.trim().startsWith("/add");
    }

    @Override
    public String execute(UserId userId, String text) {
        String trimmedText = text.trim();
        String args = trimmedText.length() > 4 ? trimmedText.substring(4).trim() : "";

        if (args.isEmpty()) {
            return "Использование: /add <дата YYYY-MM-DD>, <время HH:mm>, <предмет>, <преподаватель>, <аудитория>";
        }
        try {
            Lesson lesson = parser.parse(args);
            
            service.addLesson(userId, lesson);
            
            return String.format("Добавлено: %s %s-%s, %s, %s, ауд. %s.",
                    lesson.date(),
                    lesson.startTime(),
                    lesson.endTime(),
                    lesson.subject(),
                    lesson.teacher(),
                    lesson.room());
                    
        } catch (IllegalArgumentException e) {
            return e.getMessage() != null ? e.getMessage() : "Произошла ошибка при добавлении пары";
        }
    }
}