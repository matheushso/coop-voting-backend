package br.com.coop.voting.backend.unit;

import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import br.com.coop.voting.backend.domain.repository.PautaRepository;
import br.com.coop.voting.backend.domain.service.PautaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {

    @InjectMocks
    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @Test
    public void cadastrarPauta_dadoDescricaoValida_deveChamarCadastrarPautaComSucesso() {
        Pauta pauta = new Pauta();
        pauta.setDescricao("Descrição da Pauta");

        pautaService.cadastrarPauta(pauta);

        verify(pautaRepository, times(1)).save(pauta);
    }

    @Test
    public void cadastrarPauta_dadoDescricaoNull_deveRetornarErro() {
        Pauta pauta = new Pauta();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.cadastrarPauta(pauta));

        assertEquals("O campo descrição da Pauta deve ser preenchido.", exception.getMessage());
        verify(pautaRepository, times(0)).save(pauta);
    }

    @Test
    public void cadastrarPauta_dadoDescricaoVazia_deveRetornarErro() {
        Pauta pauta = new Pauta();
        pauta.setDescricao("   ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.cadastrarPauta(pauta));

        assertEquals("O campo descrição da Pauta deve ser preenchido.", exception.getMessage());
        verify(pautaRepository, times(0)).save(pauta);
    }

    @Test
    public void retornarPautaValida_dadoPautaNull_deveRetornarErro() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.retornarPautaValida(null));

        assertEquals("Não foi informado uma Pauta.", exception.getMessage());
        verify(pautaRepository, times(0)).findById(any());
    }

    @Test
    public void retornarPautaValida_dadoPautaSemId_deveRetornarErro() {
        Pauta pauta = new Pauta();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.retornarPautaValida(null));

        assertEquals("Não foi informado uma Pauta.", exception.getMessage());
        verify(pautaRepository, times(0)).findById(any());
    }

    @Test
    public void cadastrarSessao_dadoSessaoVotacaoNaoCadastrada_deveRetornarErro() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> pautaService.retornarPautaValida(pauta));

        assertEquals(String.format("Não encontrado Pauta com Id %d.", pauta.getId()), exception.getMessage());
        verify(pautaRepository, times(1)).findById(any());
    }
}
