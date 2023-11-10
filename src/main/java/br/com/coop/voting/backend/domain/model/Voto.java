package br.com.coop.voting.backend.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Voto {

    @Id
    private Long id;

    @OneToOne
    private Associado associado;

    private EscolhaVoto escolhaVoto;

    @ManyToOne
    private Pauta pauta;
}
