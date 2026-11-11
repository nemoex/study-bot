package ru.urfu.studyassistant.infrastructure.persistence;

import ru.urfu.studyassistant.core.model.Lesson;
import ru.urfu.studyassistant.core.model.UserId;
import ru.urfu.studyassistant.core.schedule.ScheduleRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcScheduleRepository implements ScheduleRepository {

    private final DataSource dataSource;

    public JdbcScheduleRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Lesson> findByDate(UserId userId, LocalDate date) {
        return findByDateRange(userId, date, date);
    }

    @Override
    public List<Lesson> findByDateRange(UserId userId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT lesson_date, start_time, subject, teacher, room
                FROM lessons
                WHERE user_id = ? AND lesson_date BETWEEN ? AND ?
                ORDER BY lesson_date, start_time
                """;

        List<Lesson> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId.value());
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Lesson(
                            rs.getDate("lesson_date").toLocalDate(),
                            rs.getTime("start_time").toLocalTime(),
                            rs.getString("subject"),
                            rs.getString("teacher"),
                            rs.getString("room")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при запросе расписания из БД", e);
        }
        return result;
    }

    @Override
    public void add(UserId userId, Lesson lesson) {
        String sql = """
                INSERT INTO lessons (user_id, lesson_date, start_time, end_time, subject, teacher, room)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId.value());
            ps.setDate(2, Date.valueOf(lesson.date()));
            ps.setTime(3, Time.valueOf(lesson.startTime()));
            ps.setTime(4, Time.valueOf(lesson.endTime()));
            ps.setString(5, lesson.subject());
            ps.setString(6, lesson.teacher());
            ps.setString(7, lesson.room());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении пары в БД", e);
        }
    }
}
