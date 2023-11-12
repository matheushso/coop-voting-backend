package br.com.coop.voting.backend.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pauta pauta;

    private LocalTime tempoAbertura;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFechamento;

    private Boolean resultadoPublicado;

    public SessaoVotacao() {
        this.tempoAbertura = LocalTime.ofSecondOfDay(60);
        this.dataInicio = LocalDateTime.now();
        this.resultadoPublicado = Boolean.FALSE;
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

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Boolean getResultadoPublicado() {
        return resultadoPublicado;
    }

    public void setResultadoPublicado(Boolean resultadoPublicado) {
        this.resultadoPublicado = resultadoPublicado;
    }
}
