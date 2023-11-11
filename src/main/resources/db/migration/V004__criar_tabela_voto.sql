create table voto
(
    id           bigint     not null auto_increment,
    associado_id bigint     not null,
    voto         varchar(3) not null,
    pauta_id     bigint     not null,

    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table voto
    add constraint fk_voto_associado
        foreign key (associado_id) references associado (id);

alter table voto
    add constraint fk_voto_pauta
        foreign key (pauta_id) references pauta (id);