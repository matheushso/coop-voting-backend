package br.com.coop.voting.backend.domain.enums;

public enum StatusPermissao {

    ABLE_TO_VOTE("Pode votar"),
    UNABLE_TO_VOTE("Não pode votar");

    private String descricao;

    StatusPermissao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
