package br.com.coop.voting.backend.domain.service;

import br.com.coop.voting.backend.domain.DTO.ResultadoDTO;
import br.com.coop.voting.backend.domain.enums.EscolhaVoto;
import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import br.com.coop.voting.backend.domain.model.Voto;
import br.com.coop.voting.backend.domain.repository.PautaRepository;
import br.com.coop.voting.backend.domain.repository.SessaoVotacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ResultadoService {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    public ResultadoDTO calcularResultado(Long pautaId) {
        Pauta pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new EntityNotFoundException(String.format("Não encontrado Pauta com Id %d.", pautaId)));

        ResultadoDTO resultado = new ResultadoDTO();
        resultado.setPautaDescricao(pauta.getDescricao());
        resultado.setQuantidadeSessoes(pauta.getSessoesVotacao().size());
        resultado.setTotalVotos(resultado.getTotalVotos() + pauta.getVotos().size());

        for (Voto voto : pauta.getVotos()) {
            if (voto.getVoto().equals(EscolhaVoto.SIM.toString())) {
                resultado.setVotosFavoraveis(resultado.getVotosFavoraveis() + 1);
            }

            if (voto.getVoto().equals(EscolhaVoto.NAO.toString())) {
                resultado.setVotosContrarios(resultado.getVotosContrarios() + 1);
            }
        }
        atualizarStatusSessao(pauta);

        return resultado;
    }

    private void atualizarStatusSessao(Pauta pauta) {
        for (SessaoVotacao sessaoVotacao : pauta.getSessoesVotacao()) {
            if (sessaoVotacao.getDataFechamento().isAfter(LocalDateTime.now())) {
                sessaoVotacao.setResultadoPublicado(Boolean.TRUE);
                sessaoVotacaoRepository.save(sessaoVotacao);
            }
        }
    }
}
