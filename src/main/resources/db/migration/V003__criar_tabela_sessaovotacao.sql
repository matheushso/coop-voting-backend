create table sessao_votacao
(
    id                 bigint not null auto_increment,
    pauta_id           bigint not null,
    tempo_abertura     time   not null,
    data_inicio        datetime(6) not null,
    data_fechamento    datetime(6) not null,
    resultado_publicado tinyint(1) not null,

    primary key (id)
) engine=InnoDB default charset=utf8mb4;

alter table sessao_votacao
    add constraint fk_sessao_votacao_pauta
        foreign key (pauta_id) references pauta (id);