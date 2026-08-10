CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL
);

CREATE TABLE semesters (
                           id UUID PRIMARY KEY,
                           user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                           name VARCHAR(255) NOT NULL,
                           start_date DATE,
                           end_date DATE,
                           is_archived BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at TIMESTAMP NOT NULL,
                           updated_at TIMESTAMP NOT NULL
);

CREATE TABLE courses (
                         id UUID PRIMARY KEY,
                         semester_id UUID NOT NULL REFERENCES semesters(id) ON DELETE CASCADE,
                         name VARCHAR(255) NOT NULL,
                         code VARCHAR(50) NOT NULL,
                         credit DOUBLE PRECISION NOT NULL,
                         ects DOUBLE PRECISION NOT NULL,
                         instructor VARCHAR(255),
                         course_type VARCHAR(20) NOT NULL,
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL
);

CREATE TABLE grade_items (
                             id UUID PRIMARY KEY,
                             course_id UUID NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
                             type VARCHAR(20) NOT NULL,
                             name VARCHAR(255) NOT NULL,
                             weight_percent DOUBLE PRECISION,
                             score DOUBLE PRECISION,
                             created_at TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP NOT NULL
);

CREATE TABLE letter_grade_scale (
                                    id UUID PRIMARY KEY,
                                    min_score DOUBLE PRECISION NOT NULL,
                                    max_score DOUBLE PRECISION NOT NULL,
                                    letter VARCHAR(5) NOT NULL,
                                    gpa_coefficient DOUBLE PRECISION NOT NULL,
                                    is_passing BOOLEAN NOT NULL
);

INSERT INTO letter_grade_scale (id, min_score, max_score, letter, gpa_coefficient, is_passing) VALUES
                                                                                                   (gen_random_uuid(), 90, 100, 'AA', 4.00, TRUE),
                                                                                                   (gen_random_uuid(), 85, 89.99, 'BA', 3.50, TRUE),
                                                                                                   (gen_random_uuid(), 80, 84.99, 'BB', 3.00, TRUE),
                                                                                                   (gen_random_uuid(), 75, 79.99, 'CB', 2.50, TRUE),
                                                                                                   (gen_random_uuid(), 70, 74.99, 'CC', 2.00, TRUE),
                                                                                                   (gen_random_uuid(), 65, 69.99, 'DC', 1.50, TRUE),
                                                                                                   (gen_random_uuid(), 60, 64.99, 'DD', 1.00, TRUE),
                                                                                                   (gen_random_uuid(), 55, 59.99, 'FD', 0.50, FALSE),
                                                                                                   (gen_random_uuid(), 0, 54.99, 'FF', 0.00, FALSE);