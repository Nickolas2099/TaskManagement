CREATE TABLE user_table
(
    id          SERIAL PRIMARY KEY,
    email       VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255)        NOT NULL,
    firstname   VARCHAR(255)        NOT NULL,
    surname     VARCHAR(255)        NOT NULL
);

CREATE TABLE role_table
(
    id      SERIAL PRIMARY KEY,
    title   VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE user_role
(
    id      SERIAL PRIMARY KEY,
    user_id SERIAL NOT NULL,
    role_id SERIAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_table(id),
    FOREIGN KEY (role_id) REFERENCES role_table(id)
);

CREATE TABLE status
(
    id    SERIAL PRIMARY KEY,
    title VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE priority
(
    id    SERIAL PRIMARY KEY,
    title VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE task
(
    id          SERIAL PRIMARY KEY,
    heading     VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255)        NOT NULL,
    status_id   INTEGER             NOT NULL,
    priority_id INTEGER             NOT NULL,
    created_by  SERIAL              NOT NULL,
    assigned_to SERIAL              NOT NULL,
    FOREIGN KEY (status_id)   REFERENCES status(id),
    FOREIGN KEY (priority_id) REFERENCES priority(id),
    FOREIGN KEY (created_by)  REFERENCES user_table(id),
    FOREIGN KEY (assigned_to) REFERENCES user_table(id)
);

CREATE TABLE comment
(
    id           SERIAL PRIMARY KEY,
    comment_text TEXT       NOT NULL,
    creation_time  TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id      SERIAL     NOT NULL,
    task_id      SERIAL     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_table(id),
    FOREIGN KEY (task_id) REFERENCES task(id)
);


INSERT INTO status(title) VALUES('в ожидании'),
                                ('в процессе'),
                                ('завершено');

INSERT INTO priority(title) VALUES('высокий'),
                                  ('средний'),
                                  ('низкий');

INSERT INTO role_table(title) VALUES('администратор'),
                                    ('пользователь');
