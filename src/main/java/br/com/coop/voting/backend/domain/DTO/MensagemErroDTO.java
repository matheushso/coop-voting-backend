package br.com.coop.voting.backend.domain.DTO;

public class MensagemErroDTO {

    private String erro;

    private String mensagem;

    private int status;

    public MensagemErroDTO(String erro, String mensagem, int status) {
        this.erro = erro;
        this.mensagem = mensagem;
        this.status = status;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
