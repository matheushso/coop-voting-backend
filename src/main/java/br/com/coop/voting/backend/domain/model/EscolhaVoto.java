package br.com.coop.voting.backend.domain.model;

public enum EscolhaVoto {

    SIM("Sim"),
    NAO("Não");

    private String descricao;

    EscolhaVoto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
