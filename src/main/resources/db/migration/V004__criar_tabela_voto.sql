create table voto
(
    id           bigserial primary key,
    associado_id bigint not null,
    voto         varchar(3) not null,
    pauta_id     bigint not null,

    constraint fk_voto_associado
        foreign key (associado_id)
            references associado (id),

    constraint fk_voto_pauta
        foreign key (pauta_id)
            references pauta (id)
);
