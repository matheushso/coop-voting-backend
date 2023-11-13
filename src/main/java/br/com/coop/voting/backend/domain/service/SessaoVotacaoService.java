package br.com.coop.voting.backend.domain.service;

import br.com.coop.voting.backend.domain.model.Pauta;
import br.com.coop.voting.backend.domain.model.SessaoVotacao;
import br.com.coop.voting.backend.domain.repository.SessaoVotacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class SessaoVotacaoService {

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private PautaService pautaService;

    public SessaoVotacao cadastrarSessao(SessaoVotacao sessaoVotacao, Integer tempoAberturaEmMinutos) {
        Pauta pauta = pautaService.retornarPautaValida(sessaoVotacao.getPauta());
        sessaoVotacao.setPauta(pauta);
        calcularDataFechamento(sessaoVotacao, tempoAberturaEmMinutos);
        verificarSeExisteSessaoEmAbertoParaPautaInformada(sessaoVotacao);

        return sessaoVotacaoRepository.save(sessaoVotacao);
    }

    public void verificarSeSessaoEstaEmAberto(SessaoVotacao sessaoVotacao) {
        if (sessaoVotacao.getDataFechamento().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(String.format("Pauta %s possui Sessão de votação, porém a mesma já está fechada. Aguarde a publicação do resultado dessa Sessão de Votação", sessaoVotacao.getPauta().getDescricao()));
        }
    }

    private void calcularDataFechamento(SessaoVotacao sessaoVotacao, Integer tempoAberturaEmMinutos) {
        if (tempoAberturaEmMinutos != null) {
            int horas = tempoAberturaEmMinutos / 60;
            int minutos = tempoAberturaEmMinutos % 60;
            LocalTime tempoAbertura = LocalTime.of(horas, minutos);

            sessaoVotacao.setTempoAbertura(tempoAbertura);
        }

        sessaoVotacao.setDataFechamento(obterDataFechamento(sessaoVotacao));
    }

    private LocalDateTime obterDataFechamento(SessaoVotacao sessaoVotacao) {
        return sessaoVotacao.getDataInicio().plusHours(sessaoVotacao.getTempoAbertura().getHour())
                .plusMinutes(sessaoVotacao.getTempoAbertura().getMinute())
                .plusSeconds(sessaoVotacao.getTempoAbertura().getSecond());
    }

    private void verificarSeExisteSessaoEmAbertoParaPautaInformada(SessaoVotacao sessaoVotacao) {
        SessaoVotacao sessaoVotacaoEmAberto = sessaoVotacaoRepository.buscarSessaoEmAbertoPorPauta(sessaoVotacao.getPauta().getId());

        if (sessaoVotacaoEmAberto != null) {
            verificarSeSessaoEstaEmAberto(sessaoVotacaoEmAberto);

            throw new IllegalArgumentException(String.format("Já existe uma Sessão de Votação em aberto para a pauta %s.", sessaoVotacao.getPauta().getDescricao()));
        }
    }
}
