package br.com.coop.voting.backend.domain.service;

import br.com.coop.voting.backend.domain.model.*;
import br.com.coop.voting.backend.domain.repository.AssociadoRepository;
import br.com.coop.voting.backend.domain.repository.PautaRepository;
import br.com.coop.voting.backend.domain.repository.SessaoVotacaoRepository;
import br.com.coop.voting.backend.domain.repository.VotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private  SessaoVotacaoService sessaoVotacaoService;

    public Voto cadastrarVoto(Voto voto) {
        voto.setAssociado(retornarAssociado(voto.getAssociado()));
        voto.setPauta(retornarPauta(voto.getPauta()));
        validarSeAssociadoPodeVotar(voto);
        validarSePautaPossuiSessaoEmAberto(voto.getPauta());
        voto.setVoto(EscolhaVoto.retornarVotoValido(voto.getVoto()));

        return votoRepository.save(voto);
    }

    private Associado retornarAssociado(Associado associado) {
        if (associado == null || associado.getId() == null) {
            throw new IllegalArgumentException("Não foi informado um Associado.");
        }

        return associadoRepository.findById(associado.getId()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Não encontrado Associado com Id %d.", associado.getId())));
    }

    private Pauta retornarPauta(Pauta pauta) {
        if (pauta == null || pauta.getId() == null) {
            throw new IllegalArgumentException("Não foi informado uma Pauta.");
        }

        return pautaRepository.findById(pauta.getId()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Não encontrado Pauta com Id %d.", pauta.getId())));
    }

    private void validarSeAssociadoPodeVotar(Voto voto) {
        Voto votoJaComputado = votoRepository.findFirstByAssociadoAndPauta(voto.getAssociado(), voto.getPauta());

        if (votoJaComputado != null) {
            throw new IllegalArgumentException(String.format("Associado com CPF %s já votou na pauta %s.", voto.getAssociado().getCpf(), voto.getPauta().getDescricao()));
        }
    }

    private void validarSePautaPossuiSessaoEmAberto(Pauta pauta) {
        if (pauta.getSessoesVotacao().isEmpty())  {
            throw new IllegalArgumentException(String.format("Pauta %s não possui nenhuma Sessão de votação.", pauta.getDescricao()));
        }

        SessaoVotacao sessaoVotacaoEmAberto = sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(pauta.getId());
        if (sessaoVotacaoEmAberto == null) {
            throw new IllegalArgumentException(String.format("Pauta %s não possui nenhuma Sessão de votação em aberto.", pauta.getDescricao()));
        }

        sessaoVotacaoService.verificarSeSessaoEstaEmAberto(sessaoVotacaoEmAberto);
    }
}
