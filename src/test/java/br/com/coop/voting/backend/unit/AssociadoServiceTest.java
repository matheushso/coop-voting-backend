package br.com.coop.voting.backend.unit;

import br.com.coop.voting.backend.domain.model.Associado;
import br.com.coop.voting.backend.domain.repository.AssociadoRepository;
import br.com.coop.voting.backend.domain.service.AssociadoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AssociadoServiceTest {

    @InjectMocks
    private AssociadoService associadoService;

    @Mock
    private AssociadoRepository associadoRepository;

    @Test
    public void buscarTodosAssociados_deveChamarRepositoryComSucesso() {
        associadoService.buscarTodosAssociados();

        verify(associadoRepository, times(1)).findAll();
    }

    @Test
    public void cadastrarAssociado_dadoCpfValido_deveCadastrarAssociadoComSucesso() {
        Associado associado = new Associado();
        associado.setCpf("61828628662");

        associadoService.cadastrarAssociado(associado);
        verify(associadoRepository, times(1)).save(associado);
    }

    @Test
    public void cadastrarAssociado_dadoCpfNull_deveRetornarErro() {
        Associado associado = new Associado();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> associadoService.cadastrarAssociado(associado));

        assertEquals("Não foi informado um CPF.", exception.getMessage());
        verify(associadoRepository, times(0)).save(associado);
    }

    @Test
    public void cadastrarAssociado_dadoCpfInvalido_deveRetornarErro() {
        Associado associado = new Associado();
        associado.setCpf("783630462051234514");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> associadoService.cadastrarAssociado(associado));

        assertEquals("O CPF informado deve ter 11 dígitos.", exception.getMessage());
        verify(associadoRepository, times(0)).save(associado);
    }

    @Test
    public void cadastrarAssociado_dadoCpfValidoComPontuacao_deveRetirarPontuacaoECadastrarAssociadoComSucesso() {
        Associado associado = new Associado();
        associado.setCpf("838.742.798-55");

        associadoService.cadastrarAssociado(associado);

        assertEquals("83874279855", associado.getCpf());
        verify(associadoRepository, times(1)).save(associado);
    }
}
