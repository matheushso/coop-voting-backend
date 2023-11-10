package br.com.coop.voting.backend.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Pauta {

    @Id
    private Long id;

    private String descricao;

    @OneToMany
    private List<SessaoVotacao> sessoesVotacao;
}
