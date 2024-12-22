--liquibase formatted sql

--changeset goodmn:1
create table notification_task (
    id serial primary key,
    text text not null,
    date_time timestamp not null,
    chat_id int not null
)