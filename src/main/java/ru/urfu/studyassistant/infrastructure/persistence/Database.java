package ru.urfu.studyassistant.infrastructure.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class Database {
    // пул соединений
    private final HikariDataSource dataSource;

    public Database(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(5);
        this.dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void initSchema() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             InputStream is = getClass().getClassLoader().getResourceAsStream("schema.sql")) {
            
            if (is == null) {
                throw new IllegalStateException("Файл schema.sql не найден в resources");
            }
            
            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            
            for (String statement : sql.split(";")) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }
            System.out.println("БД успешно инициализирована.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации БД", e);
        }
    }
}
