create database if not exists TodoList;
use TodoList;

CREATE TABLE if not exists task
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    deadline      BIGINT                NULL,
    `description` VARCHAR(255)          NULL,
    name          VARCHAR(255)          NULL,
    status        BIT(1)                NOT NULL,
    todo_list_id  BIGINT                NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE if not exists todo_list
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    category VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE if not exists user
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    avatar_url VARCHAR(255)          NULL,
    password   VARCHAR(255)          NULL,
    username   VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);
insert into todo_list(category)
values ('重要'),
       ('我的一天'),
       ('计划内');

ALTER TABLE task
    ADD CONSTRAINT FKfveviky2vs8r9k10b4wf56em FOREIGN KEY (todo_list_id) REFERENCES todo_list (id) ON DELETE NO ACTION;

CREATE INDEX FKfveviky2vs8r9k10b4wf56em ON task (todo_list_id);
