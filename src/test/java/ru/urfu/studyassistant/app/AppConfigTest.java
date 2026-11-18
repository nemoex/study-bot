package ru.urfu.studyassistant.app;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppConfigTest {

    @Test
    void readsValuesFromEnvironment() {
        AppConfig config = AppConfig.fromMap(Map.of(
                "VK_GROUP_ID", "123",
                "VK_BOT_TOKEN", "token",
                "DB_URL", "jdbc:postgresql://localhost:5432/study_assistant",
                "DB_USER", "postgres",
                "DB_PASSWORD", "secret"
        ));

        assertThat(config.vkGroupId()).isEqualTo("123");
        assertThat(config.vkToken()).isEqualTo("token");
        assertThat(config.dbUser()).isEqualTo("postgres");
    }

    @Test
    void failsWhenRequiredValueIsMissing() {
        assertThatThrownBy(() -> AppConfig.fromMap(Map.of()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("VK_GROUP_ID");
    }

    @Test
    void failsWhenRequiredValueIsBlank() {
        assertThatThrownBy(() -> AppConfig.fromMap(Map.of(
                "VK_GROUP_ID", "123",
                "VK_BOT_TOKEN", "   ",
                "DB_URL", "jdbc:postgresql://localhost:5432/study_assistant"
        )))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("VK_BOT_TOKEN");
    }
}