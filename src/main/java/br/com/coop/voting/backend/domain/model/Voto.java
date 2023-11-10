package br.com.coop.voting.backend.domain.model;

import jakarta.persistence.*;

@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Associado associado;

    private EscolhaVoto escolhaVoto;

    @ManyToOne
    private Pauta pauta;
}
