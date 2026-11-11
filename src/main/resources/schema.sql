CREATE TABLE IF NOT EXISTS lessons (
    id          BIGSERIAL PRIMARY KEY,
    user_id     VARCHAR(64) NOT NULL,
    lesson_date DATE        NOT NULL,
    start_time  TIME        NOT NULL,
    end_time    TIME        NOT NULL,
    subject     VARCHAR(255) NOT NULL,
    teacher     VARCHAR(255) NOT NULL,
    room        VARCHAR(64)  NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_lessons_user_date ON lessons (user_id, lesson_date);
