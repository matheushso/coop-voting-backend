create table pauta
(
    id  bigint      not null auto_increment,
    descricao varchar(80) not null,

    primary key (id)
) engine=InnoDB default charset=utf8mb4;