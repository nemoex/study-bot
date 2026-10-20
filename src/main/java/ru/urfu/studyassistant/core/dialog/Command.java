package ru.urfu.studyassistant.core.dialog;

import ru.urfu.studyassistant.core.model.UserId;

public interface Command {
    boolean matches(String text);
    String execute(UserId userId, String text);
}