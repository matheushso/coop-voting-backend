package br.com.coop.voting.backend.domain.service;

import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.repository.PautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    public Pauta cadastrarPauta(Pauta pauta) {
        if (pauta.getDescricao() == null || pauta.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo descrição da Pauta deve ser preenchido.");
        }

        return pautaRepository.save(pauta);
    }

    public Pauta retornarPautaValida(Pauta pauta) {
        if (pauta == null || pauta.getId() == null) {
            throw new IllegalArgumentException("Não foi informado uma Pauta.");
        }

        return pautaRepository.findById(pauta.getId()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Não encontrado Pauta com Id %d.", pauta.getId())));
    }
}
