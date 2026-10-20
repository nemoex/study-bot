package ru.urfu.studyassistant.core.port;

import ru.urfu.studyassistant.core.model.UserId;

public record Message(UserId userId, String text) {
}