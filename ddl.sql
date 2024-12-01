create or replace table connectevent.locations
(
    id       bigint auto_increment
        primary key,
    address  varchar(255) null,
    capacity int          null,
    name     varchar(255) not null
);

create or replace table connectevent.events
(
    id          bigint auto_increment
        primary key,
    description varchar(255) null,
    ended_at    datetime     not null,
    started_at  datetime     not null,
    title       varchar(255) not null,
    location_id bigint       not null,
    constraint FK7a9tiyl3gaugxrtjc2m97awui
        foreign key (location_id) references connectevent.locations (id)
);

create or replace table connectevent.members
(
    id            bigint auto_increment
        primary key,
    email         varchar(255) not null,
    name          varchar(255) not null,
    password      varchar(255) not null,
    registered_at datetime     not null,
    constraint UK_9d30a9u1qpg8eou0otgkwrp5d
        unique (email)
);

create or replace table connectevent.participants
(
    id            bigint auto_increment
        primary key,
    registered_at datetime     null,
    role          varchar(255) not null,
    event_id      bigint       not null,
    member_id     bigint       not null,
    constraint FKa4mcvag4jvq6b185pnffiysyw
        foreign key (event_id) references connectevent.events (id),
    constraint FKpnd4mhdnqv8o2ewk21o59eam3
        foreign key (member_id) references connectevent.members (id)
);

create or replace table connectevent.feedbacks
(
    id             bigint auto_increment
        primary key,
    comment        varchar(255) not null,
    created_at     datetime     not null,
    rating         int          not null,
    event_id       bigint       not null,
    participant_id bigint       not null,
    constraint FKfr1mdb1ux17qw5k0xobnckww0
        foreign key (event_id) references connectevent.events (id),
    constraint FKs6o4fsixfoeqiwa0rdlaau5s3
        foreign key (participant_id) references connectevent.participants (id)
);

create or replace table connectevent.tags
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);

create or replace table connectevent.event_tags
(
    id       bigint auto_increment
        primary key,
    event_id bigint null,
    tag_id   bigint null,
    constraint FKiwoyitw224ykom58m5xnoa9y6
        foreign key (event_id) references connectevent.events (id),
    constraint FKt07xql63t6125c0wjk4j0lmqa
        foreign key (tag_id) references connectevent.tags (id)
);

