create table sessao_votacao
(
    id                  bigserial primary key,
    pauta_id            bigint       not null,
    tempo_abertura      time         not null,
    data_inicio         timestamp(6) not null,
    data_fechamento     timestamp(6) not null,
    resultado_publicado boolean      not null,

    constraint fk_sessao_votacao_pauta
        foreign key (pauta_id)
            references pauta (id)
);
