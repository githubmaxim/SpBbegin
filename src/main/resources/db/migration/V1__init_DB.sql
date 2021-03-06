create table city (
    id int4 not null,
    city varchar(255),
    primary key (id)
);

create table file_info (
    id int4 not null,
    my_key varchar(255),
    name varchar(255),
    size bigint,
    upload_date date,
    primary key (id)
);

create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );


create table logpas (
    id int4 not null,
    active bit not null,
    password varchar(255),
    username varchar(255),
    primary key (id)
);

create table logpass_role (
    logpass_id int4 not null,
    roles varchar(255)
);

create table product (
    id int4 not null,
    name varchar(255),
    price float not null,
    primary key (id)
);

create table product3 (
    id binary(255) not null,
    name varchar(255),
    price float not null,
    primary key (id)
);

create table university (
    id int4 not null,
    name varchar(255),
    city_id integer,
    primary key (id)
);

create table users (
id int4 not null,
email varchar(255),
login varchar(255),
name varchar(255),
university_id integer,
primary key (id)
);

alter table logpass_role
    add constraint FKl5i57k1f9w97xx804b6j75g7y
    foreign key (logpass_id) references logpas (id);

alter table university
    add constraint FK70cg9iket7hgqy26vs3d652uw
    foreign key (city_id) references city (id);

alter table users
    add constraint FKg6bhc1g69lfy3mquw927rmr9m
    foreign key (university_id) references university (id);