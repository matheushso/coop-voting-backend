package br.com.coop.voting.backend.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalTime;
import java.util.List;

@Entity
public class SessaoVotacao {


    @Id
    private Long id;

    @ManyToOne
    private Pauta pauta;

    private LocalTime tempoAbertura;

    @OneToMany
    private List<Voto> votos;

    public SessaoVotacao() {
        this.tempoAbertura = LocalTime.ofSecondOfDay(60);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
    }

    public LocalTime getTempoAbertura() {
        return tempoAbertura;
    }

    public void setTempoAbertura(LocalTime tempoAbertura) {
        this.tempoAbertura = tempoAbertura;
    }

    public List<Voto> getVotos() {
        return votos;
    }
}
