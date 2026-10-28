package ru.urfu.studyassistant.infrastructure.vk;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VkConfigTest {

    @Test
    void storesGroupIdAndToken() {
        VkConfig config = new VkConfig("123456", "secret-token");
        assertThat(config.groupId()).isEqualTo("123456");
        assertThat(config.accessToken()).isEqualTo("secret-token");
    }

    @Test
    void rejectsBlankGroupId() {
        assertThatThrownBy(() -> new VkConfig("", "token"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("groupId");
    }

    @Test
    void rejectsBlankToken() {
        assertThatThrownBy(() -> new VkConfig("123", "  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("accessToken");
    }

    @Test
    void readsFromEnvironmentVariables() {
        VkConfig config = VkConfig.fromValues("999", "env-token");
        assertThat(config.groupId()).isEqualTo("999");
        assertThat(config.accessToken()).isEqualTo("env-token");
    }

    @Test
    void fromValuesRejectsNull() {
        assertThatThrownBy(() -> VkConfig.fromValues(null, "token"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> VkConfig.fromValues("123", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}