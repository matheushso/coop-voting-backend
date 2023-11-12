package br.com.coop.voting.backend.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;

public enum EscolhaVoto {

    SIM("Sim"),
    NAO("Nao");

    private String descricao;

    EscolhaVoto(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static String retornarVotoValido(String voto) {
        if (voto == null) {
            throw new IllegalArgumentException("Não informado voto.");
        }
        voto = removerAcentos(voto);

        for (EscolhaVoto escolha : EscolhaVoto.values()) {
            if (escolha.getDescricao().equalsIgnoreCase(voto)) {
                return escolha.toString();
            }
        }

        throw new IllegalArgumentException(String.format("Valor inválido informado para escolha de voto. Valor informado %s.", voto));
    }

    private static String removerAcentos(String voto) {
        return Normalizer.normalize(voto, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    @JsonValue
    public String toValue() {
        return descricao;
    }
}
