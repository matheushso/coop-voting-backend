package br.com.coop.voting.backend.domain.service;

import br.com.coop.voting.backend.domain.model.Associado;
import br.com.coop.voting.backend.domain.repository.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    public List<Associado> buscarTodosAssociados() {
        return associadoRepository.findAll();
    }

    public Associado cadastrarAssociado(Associado associado) {
        associado.setCpf(removerPontuacaoCpf(associado.getCpf()));
        validarCpf(associado.getCpf());
        return associadoRepository.save(associado);
    }

    private String removerPontuacaoCpf(String cpf) {
        return cpf.replaceAll("[^0-9]", "");
    }

    private void validarCpf(String cpf) {
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("O CPF informado deve ter 11 dígitos.");
        }
    }
}
