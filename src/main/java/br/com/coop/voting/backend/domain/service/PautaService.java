package br.com.coop.voting.backend.domain.service;

import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    public Pauta cadastrarPauta(Pauta pauta) {
        return pautaRepository.save(pauta);
    }
}
