package br.com.coop.voting.backend.domain.service;

import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import br.com.coop.voting.backend.domain.repository.PautaRepository;
import br.com.coop.voting.backend.domain.repository.SessaoVotacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessaoVotacaoService {

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    public SessaoVotacao cadastrarSessao(SessaoVotacao sessaoVotacao) {
        Pauta pauta = retornarPautaValida(sessaoVotacao.getPauta());
        sessaoVotacao.setPauta(pauta);
        calcularDataFechamento(sessaoVotacao);

        return sessaoVotacaoRepository.save(sessaoVotacao);
    }

    private Pauta retornarPautaValida(Pauta pauta) {
        if (pauta == null || pauta.getId() == null) {
            throw new IllegalArgumentException("Não foi informado uma Pauta.");
        }

        return pautaRepository.findById(pauta.getId()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Não encontrado Pauta com Id %d.", pauta.getId())));
    }

    private void calcularDataFechamento(SessaoVotacao sessaoVotacao) {
        sessaoVotacao.setDataFechamento(sessaoVotacao.getDataInicio().plusHours(sessaoVotacao.getTempoAbertura().getHour())
                .plusMinutes(sessaoVotacao.getTempoAbertura().getMinute())
                .plusSeconds(sessaoVotacao.getTempoAbertura().getSecond()));
    }
}
