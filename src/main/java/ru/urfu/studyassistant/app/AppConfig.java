package ru.urfu.studyassistant.app;

import java.util.Map;

public record AppConfig(String vkGroupId, String vkToken,
                        String dbUrl, String dbUser, String dbPassword) {

    public static AppConfig fromEnv() {
        return fromMap(System.getenv());
    }

    static AppConfig fromMap(Map<String, String> env) {
        return new AppConfig(
                required(env, "VK_GROUP_ID"),
                required(env, "VK_BOT_TOKEN"),
                required(env, "DB_URL"),
                env.get("DB_USER"),
                env.get("DB_PASSWORD")
        );
    }

    private static String required(Map<String, String> env, String name) {
        String value = env.get(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Ошибка: задайте переменную окружения " + name);
        }
        return value;
    }
}