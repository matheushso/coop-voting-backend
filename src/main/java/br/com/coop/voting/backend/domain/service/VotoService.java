package br.com.coop.voting.backend.domain.service;

import br.com.coop.voting.backend.domain.DTO.StatusDTO;
import br.com.coop.voting.backend.domain.enums.EscolhaVoto;
import br.com.coop.voting.backend.domain.enums.StatusPermissao;
import br.com.coop.voting.backend.domain.exception.AssociadoNaoAutorizadoException;
import br.com.coop.voting.backend.domain.model.Associado;
import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import br.com.coop.voting.backend.domain.model.Voto;
import br.com.coop.voting.backend.domain.repository.AssociadoRepository;
import br.com.coop.voting.backend.domain.repository.PautaRepository;
import br.com.coop.voting.backend.domain.repository.SessaoVotacaoRepository;
import br.com.coop.voting.backend.domain.repository.VotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private SessaoVotacaoService sessaoVotacaoService;

    @Value("${url.integracao.validar.associado}")
    protected String urlValidaCpfAssociado;


    public Voto cadastrarVoto(Voto voto) {
        validarCampos(voto);
        return votoRepository.save(voto);
    }

    public Voto cadastrarVotoValidandoCpfAssociado(Voto voto) {
        validarCampos(voto);

        RestTemplate restTemplate = new RestTemplate();
        StatusDTO statusPermissao = restTemplate.getForEntity(urlValidaCpfAssociado.concat("/").concat(voto.getAssociado().getCpf()), StatusDTO.class).getBody();

        if (statusPermissao != null) {
            if (statusPermissao.getStatus().equals(StatusPermissao.UNABLE_TO_VOTE)) {
                throw new AssociadoNaoAutorizadoException(String.format("O Associado com CPF %s não possui permissão para votar.", voto.getAssociado().getCpf()));
            }
        }

        return votoRepository.save(voto);
    }

    private void validarCampos(Voto voto) {
        voto.setAssociado(retornarAssociado(voto.getAssociado()));
        voto.setPauta(retornarPauta(voto.getPauta()));
        validarSeAssociadoPodeVotar(voto);
        validarSePautaPossuiSessaoEmAberto(voto.getPauta());
        voto.setVoto(EscolhaVoto.retornarVotoValido(voto.getVoto()));
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
        if (pauta.getSessoesVotacao() == null || pauta.getSessoesVotacao().isEmpty()) {
            throw new IllegalArgumentException(String.format("Pauta %s não possui nenhuma Sessão de votação.", pauta.getDescricao()));
        }

        SessaoVotacao sessaoVotacaoEmAberto = sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(pauta.getId());
        if (sessaoVotacaoEmAberto == null) {
            throw new IllegalArgumentException(String.format("Pauta %s não possui nenhuma Sessão de votação em aberto.", pauta.getDescricao()));
        }

        sessaoVotacaoService.verificarSeSessaoEstaEmAberto(sessaoVotacaoEmAberto);
    }
}
