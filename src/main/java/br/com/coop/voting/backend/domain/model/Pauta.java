package br.com.coop.voting.backend.domain.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @OneToMany
    private List<SessaoVotacao> sessoesVotacao;
}
