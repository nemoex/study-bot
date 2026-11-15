package ru.urfu.studyassistant.app;

import com.google.inject.Guice;
import ru.urfu.studyassistant.infrastructure.vk.VkChatPlatform;

public final class MainVk {

    public static void main(String[] args) {
        AppConfig config;
        try {
            config = AppConfig.fromEnv();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

        Guice.createInjector(new VkModule(config))
                .getInstance(VkChatPlatform.class)
                .run();
    }
}