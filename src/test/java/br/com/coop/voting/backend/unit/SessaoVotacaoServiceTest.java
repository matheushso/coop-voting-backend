package br.com.coop.voting.backend.unit;

import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import br.com.coop.voting.backend.domain.repository.SessaoVotacaoRepository;
import br.com.coop.voting.backend.domain.service.PautaService;
import br.com.coop.voting.backend.domain.service.SessaoVotacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessaoVotacaoServiceTest {

    @InjectMocks
    private SessaoVotacaoService sessaoVotacaoService;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private PautaService pautaService;

    @Test
    public void cadastrarSessao_dadoTempoAberturaEmMinutosNaoInformado_deveSetarTempoAberturaPadrao() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setPauta(new Pauta());
        when(pautaService.retornarPautaValida(sessaoVotacao.getPauta())).thenReturn(sessaoVotacao.getPauta());

        sessaoVotacaoService.cadastrarSessao(sessaoVotacao, null);

        assertEquals(LocalTime.of(0, 1), sessaoVotacao.getTempoAbertura());
    }

    @Test
    public void cadastrarSessao_dadoTempoAberturaEmMinutos_deveRetornarTempoAberturaEscolhido() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        sessaoVotacao.setPauta(new Pauta());
        when(pautaService.retornarPautaValida(sessaoVotacao.getPauta())).thenReturn(sessaoVotacao.getPauta());

        sessaoVotacaoService.cadastrarSessao(sessaoVotacao, 90);

        assertEquals(LocalTime.of(1, 30), sessaoVotacao.getTempoAbertura());
    }

    @Test
    public void cadastrarSessao_dadoSessaoVotacaoJaAberta_deveRetornarErro() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setDescricao("Descrição de Pauta");
        sessaoVotacao.setPauta(pauta);
        when(pautaService.retornarPautaValida(sessaoVotacao.getPauta())).thenReturn(sessaoVotacao.getPauta());
        when(sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(sessaoVotacao.getPauta().getId())).thenReturn(sessaoVotacao);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> sessaoVotacaoService.cadastrarSessao(sessaoVotacao, null));

        assertEquals(String.format("Já existe uma Sessão de Votação em aberto para a pauta %s.", sessaoVotacao.getPauta().getDescricao()), exception.getMessage());
        verify(sessaoVotacaoRepository, times(0)).save(sessaoVotacao);
    }

    @Test
    public void verificarSeSessaoEstaEmAberto_dadoSessaoVotacaoFechadaMasNaoEncerrada_deveRetornarErro() {
        SessaoVotacao sessaoVotacao = new SessaoVotacao();
        Pauta pauta = new Pauta();
        pauta.setDescricao("Descrição de Pauta");
        sessaoVotacao.setPauta(pauta);
        sessaoVotacao.setDataFechamento(LocalDateTime.now().minusHours(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> sessaoVotacaoService.verificarSeSessaoEstaEmAberto(sessaoVotacao));

        assertEquals(String.format("Pauta %s possui Sessão de votação, porém a mesma já está fechada. Aguarde a publicação do resultado dessa Sessão de Votação",
                sessaoVotacao.getPauta().getDescricao()), exception.getMessage());
        verify(sessaoVotacaoRepository, times(0)).save(sessaoVotacao);
    }
}
