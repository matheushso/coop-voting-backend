package br.com.coop.voting.backend.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @JsonIgnore
    @OneToMany(mappedBy = "pauta")
    private List<SessaoVotacao> sessoesVotacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<SessaoVotacao> getSessoesVotacao() {
        return sessoesVotacao;
    }

    public void setSessoesVotacao(List<SessaoVotacao> sessoesVotacao) {
        this.sessoesVotacao = sessoesVotacao;
    }
}
