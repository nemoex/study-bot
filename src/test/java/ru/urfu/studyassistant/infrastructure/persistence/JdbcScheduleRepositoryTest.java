package ru.urfu.studyassistant.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.urfu.studyassistant.core.model.Lesson;
import ru.urfu.studyassistant.core.model.UserId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcScheduleRepositoryTest {

    private JdbcScheduleRepository repository;

    @BeforeEach
    void setUp() {
        String dbName = "testdb_" + System.nanoTime(); 
        Database database = new Database("jdbc:h2:mem:" + dbName + ";MODE=PostgreSQL;DB_CLOSE_DELAY=-1", "sa", "");
        database.initSchema();
        repository = new JdbcScheduleRepository(database.getDataSource());
    }

   @Test
    void shouldSaveAndFindLesson() {
        UserId user = new UserId("vk-123");
        LocalDate date = LocalDate.of(2026, 10, 12);
        Lesson lesson = new Lesson(date, LocalTime.of(10, 15), "ООП", "Денис Матафонов", "Р-101");

        repository.add(user, lesson);
        List<Lesson> found = repository.findByDate(user, date);

        assertThat(found).hasSize(1);
        assertThat(found.get(0).subject()).isEqualTo("ООП");
        assertThat(found.get(0).teacher()).isEqualTo("Денис Матафонов");
        assertThat(found.get(0).endTime()).isEqualTo(LocalTime.of(11, 45));
    }

    @Test
    void shouldIsolateUsers() {
        UserId user1 = new UserId("vk-111");
        UserId user2 = new UserId("vk-222");
        LocalDate date = LocalDate.of(2026, 10, 12);
        
        repository.add(user1, new Lesson(date, LocalTime.of(10, 15), "ООП", "Денис Матафонов", "Р-101"));

        assertThat(repository.findByDate(user1, date)).hasSize(1);
        assertThat(repository.findByDate(user2, date)).isEmpty();
    }

    @Test
    void shouldFindByDateRange() {
        UserId user = new UserId("vk-123");
        LocalDate date1 = LocalDate.of(2026, 10, 12);
        LocalDate date2 = LocalDate.of(2026, 10, 14);
        
        repository.add(user, new Lesson(date1, LocalTime.of(10, 15), "ООП", "Денис Матафонов", "Р-101"));
        repository.add(user, new Lesson(date2, LocalTime.of(14, 15), "Алгоритмы", "Кирилл Решке", "Т-333"));

        List<Lesson> found = repository.findByDateRange(user, date1, date2);
        assertThat(found).hasSize(2);
    }
}