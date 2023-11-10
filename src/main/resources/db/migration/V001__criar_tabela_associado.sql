create table associado
(
    id  bigint      not null auto_increment,
    cpf varchar(11) not null,

    primary key (id)
) engine=InnoDB default charset=utf8mb4;