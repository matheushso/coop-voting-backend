package br.com.coop.voting.backend.domain.DTO;

import br.com.coop.voting.backend.domain.enums.StatusPermissao;

public class StatusDTO {

    private StatusPermissao status;

    public StatusPermissao getStatus() {
        return status;
    }

    public void setStatus(StatusPermissao status) {
        this.status = status;
    }
}
