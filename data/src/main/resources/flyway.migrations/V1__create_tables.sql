CREATE TABLE user (
    id int not null auto_increment primary key,
    surname varchar(40),
    name varchar(20),
    patronymic varchar(40),
    email varchar(50) NOT NULL
);

CREATE TABLE user_roles (
    user_id int not null primary key,
    roles varchar(256) DEFAULT NULL,
    foreign key (user_id) REFERENCES user(id)
);