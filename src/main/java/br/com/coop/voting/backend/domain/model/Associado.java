package br.com.coop.voting.backend.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Associado {

    @Id
    private Long id;

    private Long cpf;
}
