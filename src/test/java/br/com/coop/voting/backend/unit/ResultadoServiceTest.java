package br.com.coop.voting.backend.unit;

import br.com.coop.voting.backend.domain.DTO.ResultadoDTO;
import br.com.coop.voting.backend.domain.enums.EscolhaVoto;
import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import br.com.coop.voting.backend.domain.model.Voto;
import br.com.coop.voting.backend.domain.repository.PautaRepository;
import br.com.coop.voting.backend.domain.repository.SessaoVotacaoRepository;
import br.com.coop.voting.backend.domain.service.ResultadoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResultadoServiceTest {

    @InjectMocks
    private ResultadoService resultadoService;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Test
    public void calcularResultado_dadoPautaIdNaoEncontrado_deveRetornarErro() {
        Long pautaId = 1L;
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> resultadoService.calcularResultado(pautaId));

        assertEquals(String.format("Não encontrado Pauta com Id %d.", pautaId), exception.getMessage());
    }

    @Test
    public void calcularResultado_dadoVotoFavoravel_deveCalcularCorretamente() {
        Long pautaId = 1L;
        Pauta pauta = new Pauta();
        pauta.setDescricao("Descrição da Pauta");
        List<Voto> votos = new ArrayList<>();
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.SIM.toString());
        votos.add(voto);
        pauta.setVotos(votos);
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        ResultadoDTO resultado = resultadoService.calcularResultado(pautaId);

        assertEquals(1, resultado.getTotalVotos());
        assertEquals(1, resultado.getVotosFavoraveis());
        assertEquals(0, resultado.getVotosContrarios());
        assertEquals(0, resultado.getQuantidadeSessoes());
        assertEquals(pauta.getDescricao(), resultado.getPautaDescricao());
    }

    @Test
    public void calcularResultado_dadoVotoContrario_deveCalcularCorretamente() {
        Long pautaId = 1L;
        Pauta pauta = new Pauta();
        pauta.setDescricao("Descrição da Pauta");
        List<Voto> votos = new ArrayList<>();
        Voto voto = new Voto();
        voto.setVoto(EscolhaVoto.NAO.toString());
        votos.add(voto);
        pauta.setVotos(votos);
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        ResultadoDTO resultado = resultadoService.calcularResultado(pautaId);

        assertEquals(1, resultado.getTotalVotos());
        assertEquals(0, resultado.getVotosFavoraveis());
        assertEquals(1, resultado.getVotosContrarios());
        assertEquals(0, resultado.getQuantidadeSessoes());
        assertEquals(pauta.getDescricao(), resultado.getPautaDescricao());
    }

    @Test
    public void calcularResultado_dadoSessoesVotacaoEncerrada_deveCalcularCorretamenteEEncerrarSessao() {
        Long pautaId = 1L;
        Pauta pauta = new Pauta();
        pauta.setDescricao("Descrição da Pauta");
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setDataFechamento(LocalDateTime.now());
        sessoesVotacao.add(sessaoVotacao);
        pauta.setSessoesVotacao(sessoesVotacao);
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        ResultadoDTO resultado = resultadoService.calcularResultado(pautaId);

        assertEquals(1, resultado.getQuantidadeSessoes());
        assertEquals(pauta.getDescricao(), resultado.getPautaDescricao());
        verify(sessaoVotacaoRepository, times(1)).save(sessaoVotacao);
    }

    @Test
    public void calcularResultado_dadoSessoesVotacaoNaoEncerrada_deveNaoEncerrarSessao() {
        Long pautaId = 1L;
        Pauta pauta = new Pauta();
        pauta.setDescricao("Descrição da Pauta");
        List<SessaoVotacao> sessoesVotacao = new ArrayList<>();
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setDataFechamento(LocalDateTime.now().plusHours(1));
        sessoesVotacao.add(sessaoVotacao);
        pauta.setSessoesVotacao(sessoesVotacao);
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        ResultadoDTO resultado = resultadoService.calcularResultado(pautaId);

        assertEquals(1, resultado.getQuantidadeSessoes());
        assertEquals(pauta.getDescricao(), resultado.getPautaDescricao());
        verify(sessaoVotacaoRepository, times(0)).save(sessaoVotacao);
    }
}
