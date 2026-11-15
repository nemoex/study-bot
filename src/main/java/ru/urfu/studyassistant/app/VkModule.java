package ru.urfu.studyassistant.app;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ru.urfu.studyassistant.core.dialog.BotEngine;
import ru.urfu.studyassistant.core.schedule.ScheduleRepository;
import ru.urfu.studyassistant.infrastructure.logging.BotLogger;
import ru.urfu.studyassistant.infrastructure.persistence.Database;
import ru.urfu.studyassistant.infrastructure.persistence.JdbcScheduleRepository;
import ru.urfu.studyassistant.infrastructure.vk.VkChatPlatform;
import ru.urfu.studyassistant.infrastructure.vk.VkConfig;

import javax.sql.DataSource;

public class VkModule extends AbstractModule {

    private final AppConfig config;

    public VkModule(AppConfig config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        install(new CoreModule());
        bind(AppConfig.class).toInstance(config);
    }

    @Provides
    @Singleton
    VkConfig vkConfig() {
        return VkConfig.fromValues(config.vkGroupId(), config.vkToken());
    }

    @Provides
    @Singleton
    DataSource dataSource(BotLogger logger) {
        Database database = new Database(config.dbUrl(), config.dbUser(), config.dbPassword());
        database.initSchema();
        logger.info("БД инициализирована");
        return database.getDataSource();
    }

    @Provides
    @Singleton
    ScheduleRepository scheduleRepository(DataSource dataSource) {
        return new JdbcScheduleRepository(dataSource);
    }

    @Provides
    @Singleton
    VkChatPlatform vkChatPlatform(VkConfig vkConfig, BotLogger logger, BotEngine engine) {
        VkChatPlatform platform = new VkChatPlatform(vkConfig.groupId(), vkConfig.accessToken(), logger);
        platform.setHandler(engine);
        return platform;
    }
}