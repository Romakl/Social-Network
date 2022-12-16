create table if not exists public.usr
(
    id         bigint
    primary key,
    username   varchar(64)          not null
    constraint uk_dfui7gxngrgwn9ewee3ogtgym
    unique,
    first_name varchar(64)          not null,
    last_name  varchar(64),
    birth_date date                 not null,
    email      varchar(255)         not null
    constraint uk_g9l96r670qkidthshajdtxrqf
    unique,
    password   varchar(255)         not null,
    is_active  boolean default true not null,
    is_deleted boolean              not null,
    is_banned  boolean              not null,
    is_private boolean              not null,
    created    timestamp(6)         not null,
    updated    timestamp(6)

);

alter table public.usr
    owner to postgres;


create table public.user_role
(
    user_id bigint not null
        constraint fkfpm8swft53ulq2hl11yplpr5
            references public.usr,
    roles   varchar(255)
);

alter table public.user_role
    owner to postgres;


create table public.followers


(
    id           bigserial
        primary key,
    is_accepted  boolean,
    from_user_fk bigint
        constraint fkjav5iyljng73m5rrma7g1owtj
            references public.usr,
    to_user_fk   bigint
        constraint fkodjjlen9cxf9er96cf6d7swnq
            references public.usr
);

alter table public.followers
    owner to postgres;

-- create 5 users with 1 admin 1 moderator 3 users
-- decoded password is 12345678
insert into public.usr values (1, 'test_user1', 'Test FN', 'Test LN', '1990-01-01', 'some-email1@gmail.com', '$2a$10$kMl22QOA099MeYJ6/y8OHOoW.eRzaFB7CNsnz5kVW6T.jusJuVH/e', true, false, false, false, now(), now()),
                              (2, 'test_user2', 'Test FN', 'Test LN', '1990-01-01', 'some-email2@gmail.com', '$2a$10$kMl22QOA099MeYJ6/y8OHOoW.eRzaFB7CNsnz5kVW6T.jusJuVH/e', true, false, false, false, now(), now()),
                              (3, 'test_user3', 'Test FN', 'Test LN', '1990-01-01', 'some-email3@gmail.com', '$2a$10$kMl22QOA099MeYJ6/y8OHOoW.eRzaFB7CNsnz5kVW6T.jusJuVH/e', true, false, false, false, now(), now()),
                              (4, 'test_admin', 'Test FN', 'Test LN', '1990-01-01', 'some-email4@gmail.com', '$2a$10$kMl22QOA099MeYJ6/y8OHOoW.eRzaFB7CNsnz5kVW6T.jusJuVH/e', true, false, false, false, now(), now()),
                              (5, 'test_moderator', 'Test FN', 'Test LN', '1990-01-01', 'some-email5@gmail.com', '$2a$10$kMl22QOA099MeYJ6/y8OHOoW.eRzaFB7CNsnz5kVW6T.jusJuVH/e', true, false, false, false, now(), now());

insert into public.user_role values (1, 'ROLE_USER'), (2, 'ROLE_USER'), (3, 'ROLE_USER'), (4, 'ROLE_ADMIN'), (5, 'ROLE_MODERATOR');

insert into public.followers values (default, true, 1, 2), (default, true, 1, 3), (default, true, 2, 1), (default, true, 3, 1), (default, true, 4, 1), (default, true, 5, 1);