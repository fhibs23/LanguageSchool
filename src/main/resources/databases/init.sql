CREATE TABLE IF NOT EXISTS types
(
    id SERIAL PRIMARY KEY ,
    name TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS courses
(
    id SERIAL PRIMARY KEY ,
    types_id INTEGER NOT NULL,
    name TEXT NOT NULL ,
    price INTEGER ,
    description TEXT ,
    FOREIGN KEY (types_id) REFERENCES types (id) ON DELETE CASCADE
);
create table if not exists users
(
    id serial primary key,
    email text,
    username text,
    password text,
    role text
);
create table if not exists basket
(
    id serial primary key,
    user_id integer,
    course_id integer,
    course_count integer,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);