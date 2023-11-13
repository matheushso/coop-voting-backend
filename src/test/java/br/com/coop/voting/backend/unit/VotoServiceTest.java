package br.com.coop.voting.backend.unit;

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
import br.com.coop.voting.backend.domain.service.SessaoVotacaoService;
import br.com.coop.voting.backend.domain.service.VotoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VotoServiceTest {

    @InjectMocks
    private VotoService votoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void cadastrarVoto_dadoDadosValidos_deveSalvarVotoComSucesso() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        sessoesVotacao.add(new SessaoVotacao());
        pauta.setSessoesVotacao(sessoesVotacao);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        when(sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(pauta.getId())).thenReturn(new SessaoVotacao());

        votoService.cadastrarVoto(voto);

        verify(votoRepository, times(1)).save(voto);
    }

    @Test
    public void cadastrarVoto_dadoAssociadoNull_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals("Não foi informado um Associado.", exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
        verify(associadoRepository, times(0)).findById(any());
    }

    @Test
    public void cadastrarVoto_dadoAssociadoSemId_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = new Associado();
        associado.setCpf("68339031660");
        voto.setAssociado(associado);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals("Não foi informado um Associado.", exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
        verify(associadoRepository, times(0)).findById(any());

    }

    @Test
    public void cadastrarVoto_dadoAssociadoNaoCadastrado_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals(String.format("Não encontrado Associado com Id %d.", associado.getId()), exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
        verify(associadoRepository, times(1)).findById(associado.getId());
    }

    @Test
    public void cadastrarVoto_dadoPautaNull_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals("Não foi informado uma Pauta.", exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
        verify(pautaRepository, times(0)).findById(any());
    }

    @Test
    public void cadastrarVoto_dadoPautaSemId_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = new Pauta();
        pauta.setDescricao("Descrição de Pauta");
        voto.setPauta(pauta);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals("Não foi informado uma Pauta.", exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
        verify(pautaRepository, times(0)).findById(any());

    }

    @Test
    public void cadastrarVoto_dadoPautaNaoCadastrado_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals(String.format("Não encontrado Pauta com Id %d.", associado.getId()), exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
        verify(pautaRepository, times(1)).findById(pauta.getId());
    }

    @Test
    public void cadastrarVoto_dadoAssociadoQueJaVotouNaPauta_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        when(votoRepository.findFirstByAssociadoAndPauta(voto.getAssociado(), voto.getPauta())).thenReturn(new Voto());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals(String.format("Associado com CPF %s já votou na pauta %s.", voto.getAssociado().getCpf(), voto.getPauta().getDescricao()), exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
    }

    @Test
    public void cadastrarVoto_dadoPautaComSessaoNull_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        pauta.setSessoesVotacao(null);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals(String.format("Pauta %s não possui nenhuma Sessão de votação.", pauta.getDescricao()), exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
    }

    @Test
    public void cadastrarVoto_dadoPautaSemSessoes_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        pauta.setSessoesVotacao(new ArrayList<>());
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals(String.format("Pauta %s não possui nenhuma Sessão de votação.", pauta.getDescricao()), exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
    }

    @Test
    public void cadastrarVoto_dadoPautaComNenhumaSessaoEmAberto_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toValue());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        sessoesVotacao.add(new SessaoVotacao());
        pauta.setSessoesVotacao(sessoesVotacao);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals(String.format("Pauta %s não possui nenhuma Sessão de votação em aberto.", pauta.getDescricao()), exception.getMessage());
        verify(votoRepository, times(0)).save(voto);
    }

    @Test
    public void cadastrarVoto_dadoVotoInvalido_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto("Voto inválido");

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        sessoesVotacao.add(new SessaoVotacao());
        pauta.setSessoesVotacao(sessoesVotacao);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        when(sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(pauta.getId())).thenReturn(new SessaoVotacao());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals("Valor inválido informado para escolha de voto. Valor informado Voto invalido.", exception.getMessage());

        verify(votoRepository, times(0)).save(voto);
    }

    @Test
    public void cadastrarVoto_dadoVotoNull_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(null);

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        sessoesVotacao.add(new SessaoVotacao());
        pauta.setSessoesVotacao(sessoesVotacao);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        when(sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(pauta.getId())).thenReturn(new SessaoVotacao());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVoto(voto));

        assertEquals("Não informado voto.", exception.getMessage());

        verify(votoRepository, times(0)).save(voto);
    }

    @Test
    public void cadastrarVotoValidandoCpfAssociado_dadoCpfSemPermissao_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        sessoesVotacao.add(new SessaoVotacao());
        pauta.setSessoesVotacao(sessoesVotacao);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        when(sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(pauta.getId())).thenReturn(new SessaoVotacao());
        when(restTemplate.getForEntity(anyString(), eq(StatusDTO.class)))
                .thenReturn(ResponseEntity.ok(new StatusDTO(StatusPermissao.UNABLE_TO_VOTE)));

        AssociadoNaoAutorizadoException exception = assertThrows(AssociadoNaoAutorizadoException.class, () -> votoService.cadastrarVotoValidandoCpfAssociado(voto));

        assertEquals(String.format("O Associado com CPF %s não possui permissão para votar.", voto.getAssociado().getCpf()), exception.getMessage());

        verify(votoRepository, times(0)).save(voto);
    }

    @Test
    public void cadastrarVotoValidandoCpfAssociado_dadoErroNaIntegracao_deveRetornarErro() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        sessoesVotacao.add(new SessaoVotacao());
        pauta.setSessoesVotacao(sessoesVotacao);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        when(sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(pauta.getId())).thenReturn(new SessaoVotacao());
        when(restTemplate.getForEntity(anyString(), eq(StatusDTO.class)))
                .thenReturn(ResponseEntity.ok(null));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> votoService.cadastrarVotoValidandoCpfAssociado(voto));

        assertEquals("Erro na integração com sistema que valida se o CPF possui permissão para votar, por favor aguarde um momento e tente novamente.", exception.getMessage());

        verify(votoRepository, times(0)).save(voto);
    }

    @Test
    public void cadastrarVotoValidandoCpfAssociado_dadoCpfComPermissao_deveSalvarVotoComSucesso() {
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());

        Associado associado = mockarAssociado(voto);
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        Pauta pauta = mockarPauta(voto);
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        sessoesVotacao.add(new SessaoVotacao());
        pauta.setSessoesVotacao(sessoesVotacao);
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        when(sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(pauta.getId())).thenReturn(new SessaoVotacao());
        when(restTemplate.getForEntity(anyString(), eq(StatusDTO.class)))
                .thenReturn(ResponseEntity.ok(new StatusDTO(StatusPermissao.ABLE_TO_VOTE)));

        votoService.cadastrarVotoValidandoCpfAssociado(voto);

        verify(votoRepository, times(1)).save(voto);
    }

    private Associado mockarAssociado(Voto voto) {
        Associado associado = new Associado();
        associado.setId(1L);
        associado.setCpf("68339031660");
        voto.setAssociado(associado);
        return associado;
    }

    private static Pauta mockarPauta(Voto voto) {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setDescricao("Descrição de Pauta");
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        sessoesVotacao.add(new SessaoVotacao());
        pauta.setSessoesVotacao(sessoesVotacao);
        voto.setPauta(pauta);
        return pauta;
    }
}
