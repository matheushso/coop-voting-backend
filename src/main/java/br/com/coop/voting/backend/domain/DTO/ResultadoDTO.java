package br.com.coop.voting.backend.domain.DTO;

public class ResultadoDTO {

    private String pautaDescricao;

    private int quantidadeSessoes;

    private int totalVotos;

    private int votosFavoraveis;

    private int votosContrarios;

    public String getPautaDescricao() {
        return pautaDescricao;
    }

    public void setPautaDescricao(String pautaDescricao) {
        this.pautaDescricao = pautaDescricao;
    }

    public Integer getQuantidadeSessoes() {
        return quantidadeSessoes;
    }

    public void setQuantidadeSessoes(Integer quantidadeSessoes) {
        this.quantidadeSessoes = quantidadeSessoes;
    }

    public Integer getTotalVotos() {
        return totalVotos;
    }

    public void setTotalVotos(Integer totalVotos) {
        this.totalVotos = totalVotos;
    }

    public Integer getVotosFavoraveis() {
        return votosFavoraveis;
    }

    public void setVotosFavoraveis(Integer votosFavoraveis) {
        this.votosFavoraveis = votosFavoraveis;
    }

    public Integer getVotosContrarios() {
        return votosContrarios;
    }

    public void setVotosContrarios(Integer votosContrarios) {
        this.votosContrarios = votosContrarios;
    }
}
